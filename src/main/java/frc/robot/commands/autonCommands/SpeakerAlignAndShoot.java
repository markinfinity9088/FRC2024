// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonCommands;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoAimPivot;
import frc.robot.commands.AutoCenter;
import frc.robot.commands.AutoCenterAuto;
import frc.robot.commands.HoldSubsystemInPositionCommand;
import frc.robot.commands.TurnDegreesCommand;
import frc.robot.commands.TurnDegreesCommandAuto;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SpeakerAlignAndShoot extends SequentialCommandGroup {
  /** Creates a new SpeakerAlignAndShoot. */
  public SpeakerAlignAndShoot() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    //align, pivot, spin wheels, and go to handoff
    SequentialCommandGroup align = new SequentialCommandGroup(new AutoCenterAuto(), new TurnDegreesCommandAuto());
    ParallelCommandGroup setupForShot = new ParallelCommandGroup(align, ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt), new AutoAimPivot(), Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels((1.0));}));
    addCommands(setupForShot);

    //shoot
    ParallelCommandGroup shooting = new ParallelCommandGroup();
    shooting.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    shooting.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    shooting.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1));
    shooting.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(0.5));
    addCommands(shooting.withTimeout(1));
    addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().stop();}).withTimeout(.1));
    addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().stopShooterWheels();}).withTimeout(.1));
  }

}
