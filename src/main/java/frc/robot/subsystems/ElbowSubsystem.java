package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ElbowSubsystem extends PositionableSubsystem {
  private final CANSparkMax elbowf, elbowb;
  private static ElbowSubsystem self;

  
  private static final boolean APPLY_SAFETY = false;
  private static final int MotorDirectionForUP = 1;
  private static final double Lowest_Elbow_Position = -400;
  private static final double Highest_Elbow_Position = -5;

  private ElbowSubsystem() {
    elbowf = new CANSparkMax(Constants.ElevatorConstants.elbowFrontCanId, MotorType.kBrushless);
    elbowb = new CANSparkMax(Constants.ElevatorConstants.elbowBackCanId, MotorType.kBrushless);
    elbowf.setIdleMode(IdleMode.kBrake);
    elbowf.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    elbowb.follow(elbowf);

    super.init(elbowf);
    super.setMaxSpeed(Constants.IntakeConstants.ELBOW_MAX_SPEED);
    //super.setMinPoint(100);
    //super.setRange(140);
    elbowf.burnFlash();
    elbowb.burnFlash();
  }

  public static ElbowSubsystem getInstance() {
    return self==null? self=new ElbowSubsystem():self;
  }

  public void move(double speed) {
    // setCurrentSpeed(speed);
    //System.out.println("Elbow speed = "+getCurrentSpeed()+" original speed = "+speed);
    SmartDashboard.putNumber("ElbowSpeed", speed);
    // elbowf.set(getCurrentSpeed());
    elbowf.set(restrictSpeedForMinMax(speed));
  }

  public void stop() {
    move(0);
  }

  public double restrictSpeedForMinMax(double speed) {
    if (!APPLY_SAFETY) {
      return speed;
    }

    int sign = (int)Math.signum(speed);
    double curPosition = getPosition();

    if (sign == MotorDirectionForUP && curPosition >= Highest_Elbow_Position ) {
      speed = 0;
    }

    if (sign != MotorDirectionForUP && curPosition <= Lowest_Elbow_Position ) {
      speed = 0;
    }

    return speed;
  }
}