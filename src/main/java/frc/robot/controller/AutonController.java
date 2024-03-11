package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.IntakeCommands;
import frc.robot.commands.SwerveDriveForDeltaCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class AutonController {
    public static Command getAutonCommand() {
        //return new SwerveDriveForDeltaCommand(SwerveDriveSubsystem.getInstance(), 4,3,0);
        return IntakeCommands.sampleAutonCommand();
        //return IntakeCommands.sampleWristCommand();
        //return DriveCommands.getSimpleSwerveCommand();
    }
}