// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PivotSubsystem;

//moves pivot to particular angle in degrees
public class MovePivotToPosition extends Command {
  PivotSubsystem pivot = PivotSubsystem.getInstance();
  private double angle;
  /** Creates a new MovePivotToPosition. */
  //position is in degrees
  public MovePivotToPosition(double angle) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.angle = MathUtil.clamp(angle,pivot.getMinAngle(),pivot.getMaxAngle());
    // System.out.println("passed angle = "+angle+" clamped angle = "+this.angle);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double encoderpos = pivot.getEncoderWithAngle(angle);
    // System.out.println("Move to postion called "+this.angle);
    pivot.moveToPosition((long)encoderpos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(pivot.getPositionDegrees()-angle) <= 1);
  }
}
