package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonCommands.HandoffAndShootCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.IntakeCommands;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;

public class AutonController {
    public static Command getAutonCommand() {
        // return IntakeCommands.sampleAutonCommand();
        //return IntakeCommands.sampleWristCommand();
        //commandGroup.addCommands(new HandoffAndShootCommand());


        // commandGroup.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
       // return DriveCommands.getSimpleSwerveCommand();

       SequentialCommandGroup commandGroup = new SequentialCommandGroup();
        commandGroup.addCommands(DriveCommands.getSimpleSwerveCommand());
       // commandGroup.addCommands(IntakeCommands.rightAutonOneRingRed());

        return commandGroup;

    }
}