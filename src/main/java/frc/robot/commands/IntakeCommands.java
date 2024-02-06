package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakeSubSystem;

/** Returns a command that grabs the item */
public class IntakeCommands {

  public static Command takeRingAndSecureCommand() {
    SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    commandGroup.addCommands(new PositionElbowForIntakeCommand());
    commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().doIntake();}).withTimeout(2.0));
    return commandGroup;
  }
}
