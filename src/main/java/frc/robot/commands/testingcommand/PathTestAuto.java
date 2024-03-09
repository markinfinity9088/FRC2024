package frc.robot.commands.testingcommand;

import java.io.IOException;
import java.nio.file.Path;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class PathTestAuto {
	private Trajectory trajectory = new Trajectory();
	private SwerveDriveSubsystem m_robotDrive = SwerveDriveSubsystem.getInstance(); 

	public PathTestAuto(String jsonPath){
		try{
			Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(jsonPath);
			trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		} catch (IOException ex){
			DriverStation.reportError("Unable to open trajectory: " + jsonPath, ex.getStackTrace());
		}
	}

	public Command getCommand(){
        var thetaController =
            new ProfiledPIDController(
                AutoConstants.kPThetaController, 0.004, 0.02, AutoConstants.kThetaControllerConstraints);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        SwerveControllerCommand swerveControllerCommand =
            new SwerveControllerCommand(
                trajectory,
                m_robotDrive::getPose, // Functional interface to feed supplier
                DriveConstants.kDriveKinematics,

                // Position controllers
                new PIDController(AutoConstants.kPXController, 0, 0),
                new PIDController(AutoConstants.kPYController, 0, 0),
                thetaController,
                m_robotDrive::setModuleStates,
                m_robotDrive);

        // Reset odometry to the initial pose of the trajectory, run path following
        // command, then stop at the end.
        return Commands.sequence(
            new InstantCommand(() -> m_robotDrive.resetOdometry(trajectory.getInitialPose())),
            swerveControllerCommand,
            new InstantCommand(() -> m_robotDrive.drive(0, 0, 0, false, false)));
	}
}