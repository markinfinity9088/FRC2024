package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

public class WristSubsystem extends PositionableSubsystem {
  private final CANSparkMax wrist;
  private static WristSubsystem self;

  private WristSubsystem() {
    wrist = new CANSparkMax(Constants.IntakeConstants.intakeWristCanId, MotorType.kBrushless);
    wrist.setIdleMode(IdleMode.kBrake);
    wrist.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

    super.init(wrist);
  }

  public static WristSubsystem getInstance() {
    return self==null? self=new WristSubsystem():self;
  }

  public void move(double speed) {
    setCurrentSpeed(limitValue(speed,Constants.IntakeConstants.MAX_SPEED));
    //wrist.set(getCurrentSpeed());
    wrist.getPIDController().setReference(getCurrentSpeed(), ControlType.kVelocity);
  }

  public void stop() {
    move(0);
  }
}