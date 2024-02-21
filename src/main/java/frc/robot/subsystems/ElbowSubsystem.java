package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

public class ElbowSubsystem extends PositionableSubsystem {
  private final CANSparkMax elbowf, elbowb;
  private static ElbowSubsystem self;
  private static double speedPercent = 1.0;

  private ElbowSubsystem() {
    elbowf = new CANSparkMax(Constants.ElevatorConstants.elbowFrontCanId, MotorType.kBrushless);
    elbowb = new CANSparkMax(Constants.ElevatorConstants.elbowBackCanId, MotorType.kBrushless);
    elbowf.setIdleMode(IdleMode.kBrake);
    elbowf.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    elbowb.follow(elbowf);

    super.init(elbowf);
  }

  public static ElbowSubsystem getInstance() {
    return self==null? self=new ElbowSubsystem():self;
  }

  public void move(double speed) {
    setCurrentSpeed(limitValue(speed,Constants.IntakeConstants.MAX_SPEED));
    elbowf.set(getCurrentSpeed() * speedPercent);
  }

  public void stop() {
    move(0);
  }
}