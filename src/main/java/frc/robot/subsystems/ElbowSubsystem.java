package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElbowSubsystem extends SubsystemBase {
  private final CANSparkMax elbow;
  private static ElbowSubsystem self;
  private double currentSpeed = 0;

  private ElbowSubsystem() {
    elbow = new CANSparkMax(Constants.IntakeConstants.elbowCanId, MotorType.kBrushless);
    elbow.setIdleMode(IdleMode.kBrake);
    elbow.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
  }

  public static ElbowSubsystem getInstance() {
    if (self == null)
      self = new ElbowSubsystem();
    return self;
  }

  public void moveToPosition(double pos) {
    double currentPos = elbow.getEncoder().getPosition();
    double speed = pos - currentPos;
    System.out.println("Moving elbow to "+pos+".. CurrentPos:"+currentPos+" with speed:"+speed);

    move(speed); // makes the intake motor rotate at given speed
  }

  public boolean isAtPosition(double pos) {
    double delta = elbow.getEncoder().getPosition() - pos;
    return Math.abs(delta)<0.5;
  }

  public void move(double speed) {
    if (speed>Constants.IntakeConstants.MAX_SPEED)
      currentSpeed = Constants.IntakeConstants.MAX_SPEED;
    else if (speed < -Constants.IntakeConstants.MAX_SPEED)
      currentSpeed = -Constants.IntakeConstants.MAX_SPEED;
    elbow.set(currentSpeed=speed);
    if (speed!=0.0)
      System.out.println("Moving elbow at speed:"+currentSpeed);
  }

   public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      currentSpeed = speedSupplier.getAsDouble();
      move(currentSpeed);
    });
  }

  public void updateSimulatedPosition() {
    elbow.getEncoder().setPosition(elbow.getEncoder().getPosition()+currentSpeed/50);
  }

  public void stop() {
    move(0);
  }
}