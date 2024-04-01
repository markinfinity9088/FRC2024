// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.utils.PivotComputation;
import frc.robot.vision.limelight.LimeLightFacade;

public class AutoAimPivot extends Command {

  LimeLightFacade limelight = LimeLightFacade.getInstance();
  PivotSubsystem pivot = PivotSubsystem.getInstance();
  SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
  private int angle = 0;
  private int maxAngle = (int) pivot.getMaxAngle();
  private int minAngle = (int) pivot.getMinAngle();

  /** Creates a new AutoAimPivot. */
  public AutoAimPivot() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // double dist = Math.sqrt(Math.pow(limelight.getTX(), 2) + Math.pow(limelight.getDistanceToGoalMeters(), 2));
    double dist = limelight.getDistanceToGoalMeters();
    angle = (int) new PivotComputation().getPivotAngle(dist);
    // if (dist > 5){
    //   angle += 5;
    // } else if (dist > 3){
    //   angle += 3;
    // }
    if (angle > maxAngle){
      angle = maxAngle;
    } else if (angle < minAngle) {
      angle = minAngle;
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // SmartDashboard.putNumber("newPivotAngle", this.angle);
    pivot.moveToPosition((int) pivot.getEncoderWithAngle(angle));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return ((Math.abs(pivot.getPositionDegrees()-angle) <= 1) || (!limelight.isTargetVisible()));
  }
}
