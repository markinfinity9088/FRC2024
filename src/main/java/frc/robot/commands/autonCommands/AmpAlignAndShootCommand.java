package frc.robot.commands.autonCommands;

import org.ejml.dense.row.decompose.UtilDecompositons_CDRM;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;

public class AmpAlignAndShootCommand extends Command {
    LimeLightFacade m_limelight;
   

    public static final Pose2d AmpPose = new Pose2d(.46-8.25,5.55-4.1,new Rotation2d(180));

    public AmpAlignAndShootCommand() {
        m_limelight = new LimeLightFacade();

    }

    /*
     * Computes distance it needs to move to amp station and schedules commands to
     * Execute move and execute Amp routine
     */
    @Override
    public void initialize() {
        boolean isTargetDetected = m_limelight.isTargetVisible();
        
        if (!isTargetDetected ) {
            return;
        }
        Pose2d visionBotPose = m_limelight.getBotPose2d();
        
        //compute how much to move to go to amp
        //Pose2d relativePoseToTarget = visionBotPose.relativeTo(AmpPose);

        SwerveDriveSubsystem drive = SwerveDriveSubsystem.getInstance();
        Pose2d currPose = drive.getPose();

        double distance = m_limelight.getDistanceToGoalMeters();
        double newXPosition = currPose.getX() ;
        double newYPosition = currPose.getY()+ (distance-0.1);
        double newHeading = drive.getHeading() + 180;

        // double newXPosition = currPose.getX() + relativePoseToTarget.getX();
        // double newYPosition = currPose.getY() + relativePoseToTarget.getY();
        // double newHeading = currPose.getRotation().getDegrees();

        //loginfo(relativePoseToTarget.getX(), relativePoseToTarget.getY());
        // loginfo(newXPosition, newYPosition, newHeading);
        
        //create command compositions
        SwerveSampleMoveCommand moveToAmpBaseCmd1 = new SwerveSampleMoveCommand(drive, Units.degreesToRadians(newHeading), false, null, 5);
        moveToAmpBaseCmd1.setMaxSpeeds(0.2, 0.2);

        SwerveSampleMoveCommand moveToAmpBaseCmd = new SwerveSampleMoveCommand(drive, newXPosition, newYPosition, 0.1);
        moveToAmpBaseCmd.setMaxSpeeds(0.2, 0.2);
        //Command setToAmpPresetCmd = ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.AmpDropOff);

        SequentialCommandGroup mainCommandGroup = new SequentialCommandGroup(moveToAmpBaseCmd1,moveToAmpBaseCmd);

        CommandScheduler.getInstance().schedule(mainCommandGroup);
    }

    // void loginfo(double deltaX, double deltaY, double heading) {
    //     SmartDashboard.putNumber("DeltaXToAmp", deltaX);
    //     SmartDashboard.putNumber("DeltaYToAmo", deltaY);
    //     SmartDashboard.putNumber("DeltaRotation", heading);

    // }


    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }
}
