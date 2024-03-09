// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;



import frc.robot.subsystems.ShooterSubsystem;



public class ToggleShooterSpeedCommand extends Command {
  private ShooterSubsystem shooter;
  private double velocity; 
  public double goalVelocity;


  /** Creates a new ToggleShooterCommadns. */
  public ToggleShooterSpeedCommand(double velocity) {
    this.velocity = velocity;
    shooter = ShooterSubsystem.getInstance();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
     if(shooter.getShooterVelocity() > 0){
      goalVelocity = 0;
    } else {
      goalVelocity = velocity;
    }
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
 
    shooter.startShooterWheels(goalVelocity);
  
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooterWheels();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double difference = shooter.getShooterVelocity() - goalVelocity;
    if(difference < 0.005){
      return true;
    }
    return false;
  }
}
