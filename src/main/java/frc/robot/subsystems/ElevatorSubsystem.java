package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

public class ElevatorSubsystem extends PositionalableSubsystem {
  private final CANSparkMax elevator;
  private static ElevatorSubsystem self;
  private double currentSpeed = 0;
  RelativeEncoder rencoder;

  private ElevatorSubsystem() {
    elevator = new CANSparkMax(Constants.ElevatorConstants.elevatorCanId, MotorType.kBrushless);
    elevator.setIdleMode(IdleMode.kBrake);
    elevator.setSmartCurrentLimit(Constants.ElevatorConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    rencoder = elevator.getEncoder();
  }

  public static ElevatorSubsystem getInstance() {
    if (self == null)
      self = new ElevatorSubsystem();
    return self;
  }

  public boolean isAtPosition(double pos) {
    double delta = elevator.getEncoder().getPosition() - pos;
    return Math.abs(delta)<0.5;
  }

  public void move(double speed) {
    if (speed>Constants.ElevatorConstants.MAX_SPEED)
      currentSpeed = Constants.ElevatorConstants.MAX_SPEED;
    else if (speed < -Constants.ElevatorConstants.MAX_SPEED)
      currentSpeed = -Constants.ElevatorConstants.MAX_SPEED;
    elevator.set(currentSpeed=speed);
    if (speed!=0.0)
      System.out.println("Moving elevator at speed:"+currentSpeed);
  }

   public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      currentSpeed = speedSupplier.getAsDouble();
      move(currentSpeed);
    });
  }

  public void stop() {
    move(0);
    super.stop();
  }
}