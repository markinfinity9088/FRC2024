// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.ShooterSubsystem;

public class SpitOutRingShootSensor extends Command {
  boolean startval;
  /** Creates a new SpitOutRingSensor. */
  public SpitOutRingShootSensor() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(IntakeSubSystem.getInstance(), ShooterSubsystem.getInstance());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startval = IntakeSubSystem.getInstance().isRingDetected();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    IntakeSubSystem.getInstance().releaseToShooter();
    ShooterSubsystem.getInstance().startShooterWheels(1);
    if (!startval && IntakeSubSystem.getInstance().isRingDetected()){
      startval = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    IntakeSubSystem.getInstance().stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return startval && !IntakeSubSystem.getInstance().isRingDetected();
  }
}
