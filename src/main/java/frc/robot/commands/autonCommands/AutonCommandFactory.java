package frc.robot.commands.autonCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.commands.intake_commands.IntakeRingCommand;
import frc.robot.commands.intake_commands.ReleaseRingCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.utils.AllianceSideEnum;
import frc.robot.vision.limelight.AlignToTarget;
import frc.robot.vision.limelight.LimelightsContainer;

public class AutonCommandFactory {
    public static Command getCommandForBasicMove(AllianceSideEnum allianceSide) {
        SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
        s_drive.setMaxSpeeds(0.5, 0.5);

        int x = 10;
        int y = 0;
        if (allianceSide == AllianceSideEnum.BlueSide) {
            x = x * -1;
        } else if (allianceSide == AllianceSideEnum.RedSide) {
 
        } else {
            return null;
        }

        return new SwerveSampleMoveCommand(s_drive, 0, 1, 0, true, new Pose2d(), 0.1);
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

    public static Command goToVisionTarget() {
        return new AlignToTarget(LimelightsContainer.getInstance().getLimeLight("limelight"));
    }
}
