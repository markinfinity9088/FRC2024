// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ShooterSubsystem;



public class ToggleShooterSpeedCommand extends InstantCommand {
  private ShooterSubsystem shooter = ShooterSubsystem.getInstance();
  private double velocity;

  /** Creates a new ToggleShooterCommadns. */
  public ToggleShooterSpeedCommand() {
    this.velocity = 1;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(shooter.getShooterVelocity() < -0.1){
      shooter.stopShooterWheels();
      SmartDashboard.putNumber("GoalVelo", shooter.getShooterVelocity());
      SmartDashboard.putString("herro","shabangalang");
    } else {
      shooter.startShooterWheels(velocity);
      SmartDashboard.putNumber("GoalVelo", shooter.getShooterVelocity());
      SmartDashboard.putString("herro","bigabagabom");
    }
    
  }

}
