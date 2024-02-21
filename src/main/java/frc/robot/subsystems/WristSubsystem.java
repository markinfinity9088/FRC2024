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
  private static double speedPercent = 1.0;

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
    if (wrist.getAbsoluteEncoder(Type.kDutyCycle).getPosition() > 0.21){
      if(speed > 0){
        System.out.println("Limiting >0 speed");
        speed = 0;
      }
    }
    if(wrist.getAbsoluteEncoder(Type.kDutyCycle).getPosition() < 0.07){
      if(speed < 0){
        System.out.println("Limiting <0 speed");
        speed = 0;
      }
    }
    setCurrentSpeed(limitValue(speed, Constants.IntakeConstants.MAX_SPEED));
    wrist.set(getCurrentSpeed()* speedPercent);
  }

  public void stop() {
    move(0);
  }
}