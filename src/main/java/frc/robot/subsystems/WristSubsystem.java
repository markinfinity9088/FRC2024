package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;

public class WristSubsystem extends PositionableSubsystem {
  private final CANSparkMax wrist;
  private static WristSubsystem self;

  private static final boolean APPLY_SAFETY = false;
  private static final int MotorDirectionForUP = 1;
  private static final double Max_Wrist_Position = 2500;
  private static final double Min_Wrist_Position = 495;


  private WristSubsystem() {
    wrist = new CANSparkMax(Constants.IntakeConstants.intakeWristCanId, MotorType.kBrushless);
    wrist.setIdleMode(IdleMode.kBrake);
    wrist.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    wrist.burnFlash();

    super.init(wrist);
    //super.setPIDValues(IntakeConstants.wristP, IntakeConstants.wristI, IntakeConstants.wristD);
    super.setMaxSpeed(Constants.IntakeConstants.MAX_SPEED);
    super.hasAbsEncoder(true);

    //super.setMinPoint(100);
    //super.setRange(0);
  }

  public static WristSubsystem getInstance() {
    return self==null? self=new WristSubsystem():self;
  }


  public void move(double speed) {
    // setCurrentSpeed(speed);
    // wrist.set(getCurrentSpeed());
    wrist.set(restrictSpeedForMinMax(speed));
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
    if (sign == MotorDirectionForUP && curPosition >= Max_Wrist_Position ) {
      speed = 0;
    }

    if (sign != MotorDirectionForUP && curPosition <= Min_Wrist_Position) {
      speed = 0;
    }

    return speed;
  }
  

  
}