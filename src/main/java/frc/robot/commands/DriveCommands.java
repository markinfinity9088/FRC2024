package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DifferentialDriveSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.Constants.AutoConstants;
import frc.robot.commands.testingcommand.PathTestAuto;

public class DriveCommands {
    public static Command getSimpleSwerveCommand() {
        // System.out.println("getSwerveAutonomousCommand called");
        //return null;
        
       SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
       
        s_drive.setMaxSpeeds(0.8, 0.8);
        SequentialCommandGroup commandGroup = new SequentialCommandGroup();

        
        //commandGroup.addCommands(new PathTestAuto(AutoConstants.traj1).getCommand());

        //kp
        //commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0.7, 0, 0, true, new Pose2d(), 0.1));

        //left
        //commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, Units.inchesToMeters(30), 0, 0, true, new Pose2d(), 0.1));
        //commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, Units.degreesToRadians(62), true, new Pose2d(), 0.5));
        //commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0, Units.inchesToMeters(81), 0, true, new Pose2d(), 0.1));
        
        commandGroup.addCommands(SampleTrajectoryCommand.MakeTrajectory());
        //left, 2 rings
        // commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 1,1, 0, true, new Pose2d(), 0.1));
        // commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 1, -1, 0, true, new Pose2d(), 0.1));


        /*
        //Z shape
        //move left
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0, -1, 0, true, new Pose2d(), 0.05));
        //swerve 45 degrees right to back
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, -1, 1, 0, true, new Pose2d(), 0.05));
        //move left
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0, -1, 0, true, new Pose2d(), 0.05));
        

        //reverse Z shape
        //move left
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0, 1, 0, true, new Pose2d(), 0.05));
        //swerve 45 degrees right to back
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 1, -1, 0, true, new Pose2d(), 0.05));
        //move left
        commandGroup.addCommands(new SwerveSampleMoveCommand(s_drive, 0, 1, 0, true, new Pose2d(), 0.05));
        */

        return commandGroup;
    }

    public static Command getSwerveWithDurationsAutonCommand() {
        SequentialCommandGroup commandGroup = new SequentialCommandGroup();
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,0.05,0,0.0));
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,-0.05,0,0.0));
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,0,0.05,0.0));
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,0,-0.05,0.0));
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,0,0,0.2));
        commandGroup.addCommands(new SwerveDriveForDurationCommand(2,0.1,0.1,0.0));

       // commandGroup.addCommands(SwerveDriveSubsystem.getInstance().driveTimeCommand(5, 0.05, 0.05, 0.0, true, true));
        return commandGroup;
    }

    public static Command getDiffAutonomousCommand() {
        SequentialCommandGroup commandGroup = new SequentialCommandGroup();
        commandGroup.addCommands(new DiffDriveForDurationCommand(2,0.2,0.0));
        commandGroup.addCommands(DifferentialDriveSubsystem.getInstance().driveTimeCommand(3, 0.3, 0.4));
        return commandGroup;
    }
}