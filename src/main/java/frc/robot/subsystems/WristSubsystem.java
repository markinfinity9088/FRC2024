package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;

public class WristSubsystem extends PositionableSubsystem {
  private final CANSparkMax wrist;
  private static WristSubsystem self;

  private WristSubsystem() {
    wrist = new CANSparkMax(Constants.IntakeConstants.intakeWristCanId, MotorType.kBrushless);
    wrist.setIdleMode(IdleMode.kBrake);
    wrist.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    wrist.burnFlash();

    super.init(wrist);
    super.setPIDValues(IntakeConstants.wristP, IntakeConstants.wristI, IntakeConstants.wristD);
    super.setMaxSpeed(Constants.IntakeConstants.MAX_SPEED);
    //super.onlyRelEncoder(false);
    //super.setMinPoint(100);
    //super.setRange(140);
  }

  public static WristSubsystem getInstance() {
    return self==null? self=new WristSubsystem():self;
  }


  public void move(double speed) {
    setCurrentSpeed(speed);
    wrist.set(getCurrentSpeed());
  }

  public void stop() {
    move(0);
  }
}