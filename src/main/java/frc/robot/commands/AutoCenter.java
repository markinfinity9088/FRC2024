// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;

public class AutoCenter extends Command {
  LimeLightFacade ll = LimeLightFacade.getInstance();
  private SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
  double angle = 0; 
  TurnDegreesCommand turnCommand;
  /** Creates a new AutoCenter. */
  public AutoCenter() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    angle = Math.atan(ll.getTX() / ll.getDistanceToGoalMeters());
    turnCommand = new TurnDegreesCommand(Units.radiansToDegrees(-angle));
    SmartDashboard.putNumber("angle", Units.radiansToDegrees(angle));
    if (ll.isTargetVisible()){
      SequentialCommandGroup setup = new SequentialCommandGroup(turnCommand, new AutoAimPivotPID());
      CommandScheduler.getInstance().schedule(setup);
    }
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // SmartDashboard.putNumber("turn angle", Units.radiansToDegrees(angle));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
    
  }
}
