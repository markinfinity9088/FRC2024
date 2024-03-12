package frc.robot.commands;

import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveDriveForDeltaCommand extends Command {
  SwerveDriveSubsystem swerveSubsystem;
  double xDelta;
  double yDelta;
  double rDelta;
  double maxdrivespeed = DriveConstants.kMaxDriveSubsystemSpeed;
  double maxturnspeed = DriveConstants.kMaxAngularSpeed;
  Pose2d initialPose;
  double tolerance = 0.1;
  PIDController pid = new PIDController(0.001, 0, 0);
  double destX, destY, destR;

  public SwerveDriveForDeltaCommand(SwerveDriveSubsystem swerveSubsystem,
      double xDelta, double yDelta, double rDelta) {
    addRequirements(swerveSubsystem);

    this.swerveSubsystem = swerveSubsystem;

    this.xDelta = xDelta;
    this.yDelta = yDelta;
    this.rDelta = rDelta;
  }

  public void setMaxSpeeds(double drivespeed, double turnspeed) {
    maxdrivespeed = drivespeed;
    maxturnspeed = turnspeed;
  }

  @Override
  public void initialize() {
    initialPose = swerveSubsystem.getPose();
    destX = initialPose.getX()+xDelta;
    destY = initialPose.getX()+yDelta;
    destR = initialPose.getTranslation().getAngle().getDegrees()+rDelta;
    System.out.println("SwerveDriveForDeltaCommand init");
    System.out.println("destX:"+destX+" destY:"+destY+" destR:"+destR);
  }

  @Override
  public void execute() {
    double xs, ys, ts;

    double currentY = swerveSubsystem.getPose().getY();
    double currentX = swerveSubsystem.getPose().getX();
    double currentR = swerveSubsystem.getPose().getTranslation().getAngle().getDegrees();
    System.out.println("currentX:"+currentX+" currentY:"+currentY+" currentR:"+currentR);
    xs = pid.calculate(currentX, destX);
    ys = pid.calculate(currentY, destY);
    ts = pid.calculate(currentR, destR);
    
    swerveSubsystem.drive(xs, ys, ts, true, true);
  }

  // Stop all module motor movement when command ends
  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stopModules();
  }

  @Override
  public boolean isFinished() {
    double currentPoseY = swerveSubsystem.getPose().getY();
    double currentPoseX = swerveSubsystem.getPose().getX();
    double ydiff = Math.abs(currentPoseY-destY);
    double xdiff = Math.abs(currentPoseX-destX);
    System.out.println("XDiff:"+xdiff+" yDoff:"+ydiff);
    SmartDashboard.putNumber("YDIFF", ydiff);
    SmartDashboard.putNumber("XDIFF", xdiff);

    return (xdiff <= tolerance && ydiff <= tolerance);  
  }
}