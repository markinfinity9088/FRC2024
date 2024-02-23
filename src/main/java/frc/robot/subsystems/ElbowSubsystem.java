package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

public class ElbowSubsystem extends PositionableSubsystem {
  private final CANSparkMax elbowf, elbowb;
  private static ElbowSubsystem self;

  private ElbowSubsystem() {
    elbowf = new CANSparkMax(Constants.ElevatorConstants.elbowFrontCanId, MotorType.kBrushless);
    elbowb = new CANSparkMax(Constants.ElevatorConstants.elbowBackCanId, MotorType.kBrushless);
    elbowf.setIdleMode(IdleMode.kBrake);
    elbowf.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    elbowb.follow(elbowf);

    super.init(elbowf);
    super.setMaxSpeed(Constants.IntakeConstants.MAX_SPEED);
    //super.setMinPoint(100);
    //super.setRange(140);
  }

  public static ElbowSubsystem getInstance() {
    return self==null? self=new ElbowSubsystem():self;
  }

  public void move(double speed) {
    setCurrentSpeed(speed);
    elbowf.set(getCurrentSpeed());
  }

  public void stop() {
    move(0);
  }
}