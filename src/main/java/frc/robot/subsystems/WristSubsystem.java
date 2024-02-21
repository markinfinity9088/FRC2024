package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder.Type;
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
    super.setMaxSpeed(0.7);
    super.setMinPoint(100);
    super.setRange(140);
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