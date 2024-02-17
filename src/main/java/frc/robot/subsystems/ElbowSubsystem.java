package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

public class ElbowSubsystem extends PositionalableSubsystem {
  private final CANSparkMax elbow;
  private static ElbowSubsystem self;
  private double currentSpeed = 0;
  private SparkPIDController m_pidController;
  private RelativeEncoder rencoder;
  private double arencoderDifference;

  private ElbowSubsystem() {
    elbow = new CANSparkMax(Constants.IntakeConstants.elbowCanId, MotorType.kBrushless);
    elbow.setIdleMode(IdleMode.kBrake);
    elbow.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

    rencoder = elbow.getEncoder();

    m_pidController = elbow.getPIDController();
    m_pidController.setFeedbackDevice(rencoder);
    // set PID coefficients
    m_pidController.setP(0.1);
    m_pidController.setI(1e-4);
    m_pidController.setD(1);
    m_pidController.setIZone(0);
    m_pidController.setFF(0);
    m_pidController.setOutputRange(1, -1);

    super.init(elbow);

    System.out.println("Elbow created with encoder diff:"+arencoderDifference);
  }

  public static ElbowSubsystem getInstance() {
    if (self == null)
      self = new ElbowSubsystem();
    return self;
  }

  public void move(double speed) {
    if (speed>Constants.IntakeConstants.MAX_SPEED)
      currentSpeed = Constants.IntakeConstants.MAX_SPEED;
    else if (speed < -Constants.IntakeConstants.MAX_SPEED)
      currentSpeed = -Constants.IntakeConstants.MAX_SPEED;
    elbow.set(currentSpeed=speed);
    if (speed!=0.0)
      System.out.println("Moving elbow at speed:"+currentSpeed);
  }

   public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      currentSpeed = speedSupplier.getAsDouble();
      move(currentSpeed);
    });
  }

  public void stop() {
    move(0);
    super.stop();
  }

  public void initSimulation() {
    System.out.println("Adding Elbow to simulation");
    REVPhysicsSim.getInstance().addSparkMax(elbow, 2.6F, 5676.0F); // DCMotor.getNEO(1));
  } 
}