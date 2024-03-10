package frc.robot.commands;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class SampleTrajectoryCommand {

    static final PIDController vXController = new PIDController(1, 0.006, 0.008);
    static final PIDController vYController = new PIDController(1, 0.006, 0.008);
    static final PIDController vThetaController =  new PIDController(1, 0.004, 0.008);

    static final ProfiledPIDController thetaController = new ProfiledPIDController(
                1, 0.004, 0.002, AutoConstants.kThetaControllerConstraints);

    static final TrajectoryConfig config =
            new TrajectoryConfig(
                    AutoConstants.kMaxSpeedMetersPerSecond,
                    AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                // Add kinematics to ensure max speed is actually obeyed
                .setKinematics(DriveConstants.kDriveKinematics);

    static SwerveDriveSubsystem m_robotDrive = SwerveDriveSubsystem.getInstance();

    public static Command MakeTrajectory(){

        // An example trajectory to follow. All units in meters.
        Trajectory exampleTrajectory =
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                // List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
                List.of(),
                // End 3 meters straight ahead of where we started, facing forward
                //new Pose2d(Units.inchesToMeters(30), 0, new Rotation2d(Units.radiansToDegrees(62))),
                new Pose2d(1, 0, new Rotation2d(Units.radiansToDegrees(62))),
                // Pass config
                config);

        
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        SwerveControllerCommand swerveControllerCommand =
            new SwerveControllerCommand(
                exampleTrajectory,
                m_robotDrive::getPose, // Functional interface to feed supplier
                DriveConstants.kDriveKinematics,

                // Position controllers
                vXController,
                vYController,
                thetaController,
                m_robotDrive::setModuleStates,
                m_robotDrive);

        // Reset odometry to the initial pose of the trajectory, run path following
        // command, then stop at the end.
        return Commands.sequence(
            new InstantCommand(() -> m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose())),
            swerveControllerCommand,
            new InstantCommand(() -> m_robotDrive.drive(0, 0, 0, false, false)));
    }
}