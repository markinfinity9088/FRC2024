package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

public class PivotSubsystem extends PositionableSubsystem {
  private final CANSparkMax pivot;
  private static PivotSubsystem self;

  private PivotSubsystem() {
    pivot = new CANSparkMax(Constants.ShooterConstants.shooterPivotCanId, MotorType.kBrushless);
    pivot.setIdleMode(IdleMode.kBrake);
    pivot.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

    super.init(pivot);
    super.setMaxSpeed(0.7);
    super.setMinPoint(100);
    super.setRange(140);  
  }

  public static PivotSubsystem getInstance() {
    return self==null ? self = new PivotSubsystem() : self;
  }

  public void move(double speed) {
    setCurrentSpeed(limitValue(speed, Constants.IntakeConstants.MAX_SPEED));
    pivot.set(getCurrentSpeed());
  }

  public void stop() {
    move(0);
  }
}