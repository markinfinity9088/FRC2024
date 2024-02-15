package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElbowSubsystem extends SubsystemBase implements PositionalableSubsystem {
  private final CANSparkMax elbow;
  private static ElbowSubsystem self;
  private double currentSpeed = 0;
  private SparkPIDController m_pidController;
  private AbsoluteEncoder aencoder;
  private RelativeEncoder rencoder;
  private double arencoderDifference;

  private ElbowSubsystem() {
    elbow = new CANSparkMax(Constants.IntakeConstants.elbowCanId, MotorType.kBrushless);
    elbow.setIdleMode(IdleMode.kBrake);
    elbow.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    rencoder = elbow.getEncoder();
    aencoder = elbow.getAbsoluteEncoder(Type.kDutyCycle);

    m_pidController = elbow.getPIDController();
    m_pidController.setFeedbackDevice(rencoder);
    arencoderDifference = rencoder.getPosition() - aencoder.getPosition();
    // set PID coefficients
    m_pidController.setP(0.1);
    m_pidController.setI(1e-4);
    m_pidController.setD(1);
    m_pidController.setIZone(0);
    m_pidController.setFF(0);
    m_pidController.setOutputRange(1, -1);

    System.out.println("Elbow created with encoder diff:"+arencoderDifference);
  }

  public static ElbowSubsystem getInstance() {
    if (self == null)
      self = new ElbowSubsystem();
    return self;
  }

  public void moveToPosition(double pos) {
    pos += arencoderDifference; // Convert to relative
    double currentPos = rencoder.getPosition();
    
    currentSpeed = pos - currentPos; // Just to help with simulation
     
    m_pidController.setReference(pos, ControlType.kPosition);
    System.out.println("Moving elbow to "+pos+".. CurrentPos:"+currentPos+" with velocity:"+rencoder.getVelocity());

    //move(speed); // makes the intake motor rotate at given speed*/
  }

  public boolean isAtPosition(double pos) {
    pos += arencoderDifference; // Convert to relative
    double delta = rencoder.getPosition() - pos;
    return Math.abs(delta)<0.5;
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

  public void updateSimulatedPosition() {
    rencoder.setPosition(rencoder.getPosition()+currentSpeed/5);
  }

  public void stop() {
    move(0);
  }

  public void initSimulation() {
    System.out.println("Adding Elbow to simulation");
    REVPhysicsSim.getInstance().addSparkMax(elbow, 2.6F, 5676.0F); // DCMotor.getNEO(1));
  }
}