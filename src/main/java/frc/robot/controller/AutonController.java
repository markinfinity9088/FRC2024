package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.IntakeCommands;
import frc.robot.commands.DriveCommands;

public class AutonController {
    public static Command getAutonCommand() {
        // return IntakeCommands.sampleAutonCommand();
        //return IntakeCommands.sampleWristCommand();
        return DriveCommands.getSimpleSwerveCommand();
    }
}