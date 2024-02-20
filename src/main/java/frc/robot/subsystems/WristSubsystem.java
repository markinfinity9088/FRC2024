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
  private static double speedPercent = 0.7;

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
    if (wrist.getAbsoluteEncoder(Type.kDutyCycle).getPosition() > 0.2){
      if(speed < 0){
        speed = -speed *0.01;
      }
    }
    if(wrist.getAbsoluteEncoder(Type.kDutyCycle).getPosition() < 0.07){
      if(speed > 0){
        speed = -speed *0.01;
      }
    }
    setCurrentSpeed(limitValue(speed,Constants.IntakeConstants.MAX_SPEED));
    // wrist.getPIDController().setReference(-getCurrentSpeed() * speedPercent, ControlType.kVelocity);
    wrist.set(-getCurrentSpeed() * speedPercent);
  }

  public void stop() {
    move(0);
  }
}