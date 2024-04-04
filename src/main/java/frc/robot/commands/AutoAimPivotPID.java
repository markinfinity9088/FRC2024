// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.utils.PivotComputation;
import frc.robot.vision.limelight.LimeLightFacade;

public class AutoAimPivotPID extends Command {
  private PIDController pivotController;
  private PivotSubsystem pivot = PivotSubsystem.getInstance();
  private double angle;
  LimeLightFacade ll = LimeLightFacade.getInstance();

  /** Creates a new MovePivotToPositionPID. */
  public AutoAimPivotPID() {
    addRequirements(pivot);
    pivotController = new PIDController(.5,.2,0); //2 3 0
    // pivotController = new PIDController(.9,.3,0.1);
    pivotController.setTolerance(Units.degreesToRadians(0.2),0.01);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double dist = ll.getDistanceToGoalMeters();
    angle = new PivotComputation().getPivotAngle(dist);
    angle = MathUtil.clamp(angle, pivot.getMinAngle(), pivot.getMaxAngle());
    SmartDashboard.putNumber("calc angle", angle);
    pivotController.setSetpoint(Units.degreesToRadians(angle));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double velocity = -pivotController.calculate(Units.degreesToRadians(pivot.getPositionDegrees()));
    pivot.move(velocity);
    //pivot.moveNoRestrictions(velocity);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pivot.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pivotController.atSetpoint();
  }
}
