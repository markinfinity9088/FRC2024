// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GeneralConstants;
import frc.robot.subsystems.IntakeSubSystem;

public class DetectRing extends Command {
  IntakeSubSystem intake = IntakeSubSystem.getInstance();
  long lastKnownDetectionTime = 0;
  /** Creates a new DetectRing. */
  public DetectRing() {
    // Use addRequirements() here to declare subsystem dependencies.
    //addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    lastKnownDetectionTime = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!intake.isRingDetected()){
      lastKnownDetectionTime = 0;
    } else if (lastKnownDetectionTime ==0) {
      lastKnownDetectionTime = System.currentTimeMillis();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("detected ring");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (GeneralConstants.kIntakeVerbose) {
      long detectedTime = lastKnownDetectionTime!=0 ? System.currentTimeMillis()-lastKnownDetectionTime : 0;
      System.out.println(" DetectRing time since lastdetection "+ detectedTime);
    }
    //If beam break sensor is on for more than 200 ms
    return (lastKnownDetectionTime!=0 && (System.currentTimeMillis()-lastKnownDetectionTime)>80);
  }
}
