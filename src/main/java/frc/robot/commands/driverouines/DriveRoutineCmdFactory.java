package frc.robot.commands.driverouines;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.commands.TurnDegreesCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;

//Work in progress
public class DriveRoutineCmdFactory {

    //Move to left or right depending on blue or red and drive all way to pickup ring 
    public static Command createDriveToPickupCmd() {
        LimeLightFacade limeLightFacade = LimeLightFacade.getInstance();
        if (!limeLightFacade.isTargetVisible()) {
            return new SequentialCommandGroup();
        }

        Pose2d cameraPose2d = limeLightFacade.getBotPose2d();

        if (DriveRoutineConstants.reset_pose_with_camera) {
            SwerveDriveSubsystem.getInstance().resetOdometry(cameraPose2d);
        } else {
            //TODO get difference between current pose and camera pose
        }

        SequentialCommandGroup commandSequence = new SequentialCommandGroup();
        //turn 
        SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
        Pose2d botpose = swerve.getPose();
        double angleToTurn = Math.abs(botpose.getRotation().getDegrees()) ; //Turn to pose 0
        commandSequence.addCommands(new TurnDegreesCommand(angleToTurn));
        //commandSequence.addCommands(new SwerveSampleMoveCommand(swerve, angleToTurn, false, null, 1));

        //move left
        commandSequence.addCommands(new SwerveSampleMoveCommand(swerve, botpose.getX(), DriveRoutineConstants.StartY, 0.3));

        //move forward to pickup
        commandSequence.addCommands(new SwerveSampleMoveCommand(swerve, DriveRoutineConstants.PickupX, DriveRoutineConstants.StartY, 0.3));

        return commandSequence;
    }



    //////
    public static Command createDriveToShootCmd() {
        SequentialCommandGroup commandSequence = new SequentialCommandGroup();
        //turn 
        SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
        Pose2d botpose = swerve.getPose();
        double angleToTurn = Math.abs(botpose.getRotation().getDegrees()) ; //Turn to pose 0
        commandSequence.addCommands(new SwerveSampleMoveCommand(swerve, angleToTurn, false, null, 1));

        //move backward to shoot
        commandSequence.addCommands(new SwerveSampleMoveCommand(swerve, botpose.getX()-DriveRoutineConstants.distanceToShootFromPickup, botpose.getY(), 0.3));

        return commandSequence;
    }

    
    //simple circle and drive bit forward at same time
    public static Command createCircleAroundAndMoveCmd(boolean forward) {
        ParallelCommandGroup parallelCmds = new ParallelCommandGroup();
        //turn 
        SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
        Pose2d botpose = swerve.getPose();
        parallelCmds.addCommands(new TurnDegreesCommand(360));

        //Move forward or backward 0.5 meters
        double metersToMove = 0.5;
        double sign = forward ? 1.0 : -1.0;
        parallelCmds.addCommands(new SwerveSampleMoveCommand(swerve, botpose.getX()+sign*metersToMove, botpose.getY(), 0.2));

        return parallelCmds;
    }

    public static Command createSimpleMoveCmd(double distance, boolean forward) {
        SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
        Pose2d botpose = swerve.getPose();
        double sign = forward ? 1.0 : -1.0;
        return new SwerveSampleMoveCommand(swerve, botpose.getX()+sign*distance, botpose.getY(), 0.2);

    }

    public static Command createSimpleSwerveCmd(double distance, boolean left) {
        SwerveDriveSubsystem swerve = SwerveDriveSubsystem.getInstance();
        Pose2d botpose = swerve.getPose();
        double sign = left ? 1.0 : -1.0;
        return new SwerveSampleMoveCommand(swerve, botpose.getX(), botpose.getY()+sign*distance, 0.2);

    }

}
