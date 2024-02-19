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
    init(elevator);
  }

  public static ElevatorSubsystem getInstance() {
    return self==null? self=new ElevatorSubsystem():self;
  }

  public void move(double speed) {
    double currentSpeed;
    elevator.set(currentSpeed=limitValue(speed,Constants.IntakeConstants.MAX_SPEED));
    if (currentSpeed!=0.0)
      System.out.println("Moving elevator at speed:"+currentSpeed);
  }

  public void stop() {
    move(0);
  }
}