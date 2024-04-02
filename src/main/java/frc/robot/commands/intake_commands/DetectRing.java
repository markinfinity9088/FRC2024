// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubSystem;

public class DetectRing extends Command {
  IntakeSubSystem intake = IntakeSubSystem.getInstance();
  double inARow = 0;
  boolean prev = false;
  /** Creates a new DetectRing. */
  public DetectRing() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (prev && intake.isRingDetected()){
      inARow += 1;
    } else {
      inARow = 0;
    }
    prev = intake.isRingDetected();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    inARow = 0;
    prev = false;
    System.out.println("detected ring");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (inARow > 15);
  }
}
