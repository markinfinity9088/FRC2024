package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;

public class ElevatorSubsystem extends PositionableSubsystem {
  private final CANSparkMax elevator;
  private static ElevatorSubsystem self;

  private ElevatorSubsystem() {
    elevator = new CANSparkMax(Constants.ElevatorConstants.elevatorCanId, MotorType.kBrushless);
    elevator.setIdleMode(IdleMode.kBrake);
    elevator.setSmartCurrentLimit(Constants.ElevatorConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    super.init(elevator);
    super.setMaxSpeed(Constants.ElevatorConstants.MAX_SPEED);
  }

  public static ElevatorSubsystem getInstance() {
    return self==null? self=new ElevatorSubsystem():self;
  }

  public void move(double speed) {
    setCurrentSpeed(speed);
    elevator.set(getCurrentSpeed());
  }

  public void stop() {
    move(0);
  }
}