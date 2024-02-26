package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubSystem extends SubsystemBase {
  static final double intakeSpeed = 1.0;
  static final double releaseToAMPSpeed = -0.8;
  static final double releaseToShooterSpeed = 1.0;
  static IntakeSubSystem self;
  static final int INTAKE_CURRENT_LIMIT_A = 30; // How many amps the intake can use while picking up
  private CANSparkMax intake = null;

  private IntakeSubSystem() {
    intake = new CANSparkMax(Constants.IntakeConstants.intakeCanId, MotorType.kBrushless);
    intake.setIdleMode(IdleMode.kBrake);
    intake.setSmartCurrentLimit(INTAKE_CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    intake.burnFlash();
  }

  public static IntakeSubSystem getInstance() {
    if (self == null && Constants.IntakeConstants.intakeCanId >= 0)
      self = new IntakeSubSystem();
    return self;
  }

  public void doIntake() {
    doIntake(intakeSpeed);
  }

  public void doIntake(double intakeSpeed) {
    if (intakeSpeed!=0) System.out.println("Intake in progress");
    intake.set(intakeSpeed); // makes the intake motor rotate at given speed
  }

  public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      double speed = speedSupplier.getAsDouble();
      doIntake(speed);
    });
  }

  public void releaseToAMP() {
    System.out.println("Releasing to AMP..");
    intake.set(releaseToAMPSpeed);
  }

  public void releaseToShooter() {
    System.out.println("Releasing to Shooter..");
    intake.set(releaseToShooterSpeed);
  }

  public void stop() {
    System.out.println("Stopping...");
    intake.set(0);
  }
}