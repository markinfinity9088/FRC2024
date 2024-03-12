package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonCommands.AutonCommandFactory;
import frc.robot.commands.autonCommands.HandoffAndShootCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.IntakeCommands;
import frc.robot.commands.SwerveDriveForDeltaCommand;
import frc.robot.subsystems.SwerveDriveSubsystem;

import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutine;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;

public class AutonController {
    public static Command getAutonCommand() {
        // return IntakeCommands.sampleAutonCommand();
        //return IntakeCommands.sampleWristCommand();
        //return new SwerveDriveForDeltaCommand(SwerveDriveSubsystem.getInstance(), 4,3,0);
        ArmRoutineCommandFactory arm = ArmRoutineCommandFactory.getInstance();


        // commandGroup.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
        return DriveCommands.getSimpleSwerveCommand();

       /*
        
       SequentialCommandGroup leftRed = new SequentialCommandGroup();
        leftRed.addCommands(new HandoffAndShootCommand());
       ParallelCommandGroup intakeAndMove = new ParallelCommandGroup();
        intakeAndMove.addCommands(DriveCommands.getSimpleSwerveCommand());
        intakeAndMove.addCommands(arm.executeArmRoutine(ArmPresets.PickupRing));
        intakeAndMove.addCommands(AutonCommandFactory.runIntakeUntilRingDetected());
        leftRed.addCommands(intakeAndMove);
        leftRed.addCommands(new HandoffAndShootCommand());


       // commandGroup.addCommands(IntakeCommands.rightAutonOneRingRed());

        return leftRed;

        */

    }
}