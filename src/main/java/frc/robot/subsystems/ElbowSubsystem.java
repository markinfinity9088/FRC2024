package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

public class ElbowSubsystem extends PositionableSubsystem {
  private final CANSparkMax elbow;
  private static ElbowSubsystem self;

  private ElbowSubsystem() {
    elbow = new CANSparkMax(Constants.IntakeConstants.elbowCanId, MotorType.kBrushless);
    elbow.setIdleMode(IdleMode.kBrake);
    elbow.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

    super.init(elbow);
  }

  public static ElbowSubsystem getInstance() {
    return self==null? self=new ElbowSubsystem():self;
  }

  public void move(double speed) {
    double currentSpeed;
    elbow.set(currentSpeed=limitValue(speed,Constants.IntakeConstants.MAX_SPEED));
    if (currentSpeed!=0.0)
      System.out.println("Moving elbow at speed:"+currentSpeed);
  }

  public void stop() {
    move(0);
  }
}