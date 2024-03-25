package frc.robot.commands.autonCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AlignToTarget;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.commands.intake_commands.IntakeRingCommand;
import frc.robot.commands.intake_commands.ReleaseRingCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.utils.AllianceSideEnum;
import frc.robot.vision.limelight.LimelightsContainer;

public class AutonCommandFactory {
    public static Command getCommandForBasicMove(AllianceSideEnum allianceSide) {
       return null;
    }


    public static Command getCommandForMoveToSpeakerAndShoot(AllianceSideEnum allianceSide) {
        return null;
    }

    public static Command getCommandForPickupRing(AllianceSideEnum allianceSide) {
        return null;
    }

    public static Command runIntakeUntilRingDetected() {
        return new IntakeRingCommand(true);
    }

    public static Command runReleaseUntilRingNotDetected() {
        return new ReleaseRingCommand(true);
    }

    //Goes to amp and sets arm
    public static Command getAmpAlignAndSetArmCommand() {

        return new AmpAlignAndShootCommand();
    }

    public static Command goToVisionTarget() {
        return new AlignToTarget(LimelightsContainer.getInstance().getLimeLight("limelight"));
    }
}
