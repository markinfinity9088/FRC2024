// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class MoveCommand extends Command {
  private ProfiledPIDController thetaController;
  private ProfiledPIDController xController;
  private ProfiledPIDController yController;

  private double kPXControllerCone = 1.1;
  private double kPYControllerCone = 1.1;

  private double kPThetaController = 2.2;

  private double xControllerGoalCone = 0.42;

  private SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
  private double startHeading;

  /** Creates a new MoveCommand. */
  public MoveCommand(double degrees, Pose2d pose) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_drive);

    xController = new ProfiledPIDController(kPXControllerCone, 0, 0, new Constraints(3, 3));
    yController = new ProfiledPIDController(kPYControllerCone, 0, 0, new Constraints(3, 3));
    thetaController = new ProfiledPIDController(kPThetaController, 0, 0, new Constraints(5, 10));
    
    thetaController.setGoal(Units.degreesToRadians(degrees));
    thetaController.setTolerance(Units.degreesToRadians(1),0.01);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.startHeading = s_drive.getHeadingRadians();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_drive.drive(0, 0, thetaController.calculate(s_drive.getHeadingRadians()-startHeading), false, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_drive.drive(0, 0, 0, false, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return thetaController.atGoal();
  }
}
