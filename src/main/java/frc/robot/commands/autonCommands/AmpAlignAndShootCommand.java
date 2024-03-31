package frc.robot.commands.autonCommands;

import org.ejml.dense.row.decompose.UtilDecompositons_CDRM;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.commands.TurnDegreesCommand;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;
import frc.robot.vision.limelight.LimelightConstants;

public class AmpAlignAndShootCommand extends Command {
    LimeLightFacade m_limelight;
    boolean m_debug = true;

    public static final Pose2d AmpPose = new Pose2d(.46-8.25,5.55-4.1,new Rotation2d(180));

    public AmpAlignAndShootCommand() {
        m_limelight = LimeLightFacade.getInstance();

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
        
        //compute how much to move to go to amp
        SwerveDriveSubsystem drive = SwerveDriveSubsystem.getInstance();
        Pose2d currPose = drive.getPose();

        double distance = m_limelight.getDistanceToGoalMeters() - 0.3;
        double newXPosition = currPose.getX() ;
        double newYPosition = currPose.getY()+ distance;

    
        loginfo(newXPosition, newYPosition, distance);
        
        Command turnTowardsAmpCmd = new TurnDegreesCommand(180);
       
        newXPosition = currPose.getX() ;
        newYPosition = currPose.getY()+ distance;
        SwerveSampleMoveCommand moveToAmpBaseCmd = new SwerveSampleMoveCommand(drive, newXPosition, newYPosition, 0.1);
        
        moveToAmpBaseCmd.setMaxSpeeds(0.2, 0.2);

        SequentialCommandGroup mainCommandGroup = new SequentialCommandGroup(turnTowardsAmpCmd,moveToAmpBaseCmd);

        CommandScheduler.getInstance().schedule(mainCommandGroup);
        
        //Command setToAmpPresetCmd = ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.AmpDropOff);

       // SequentialCommandGroup mainCommandGroup = new SequentialCommandGroup(moveToAmpBaseCmd1,moveToAmpBaseCmd);

        //CommandScheduler.getInstance().schedule(mainCommandGroup);
    }

     void loginfo(double deltaX, double deltaY, double distance) {
        if (m_debug) {
            SmartDashboard.putNumber("DeltaXToAmp", deltaX);
            SmartDashboard.putNumber("DeltaYToAmo", deltaY);
            SmartDashboard.putNumber("DeltaDistance", distance);
        }
         

     }


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
