package frc.robot.commands.autonCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.SwerveSampleMoveCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.utils.AllianceSideEnum;

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
}
