package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.subsystems.WristSubsystem;

/** Returns a command that grabs the item */
public class IntakeCommands {
  final static double wristIntakePosition = 0.20;
  final static double elbowIntakePosition = 0.20;
  final static double elevatorIntakePosition = 0;
  final static double pulleyIntakePosition = 0;

  final static  double elbowSecurePosition = 5.5;

  final static double elbowAMPPosition = 10;
  final static double elevatorAMPPosition = 10;
  final static double pulleyAMPPosition = 10;


  public static Command takeRingAndSecureCommand() {
    SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    commandGroup.addCommands(new PositionSubsystemCommand(wristIntakePosition, WristSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(elevatorIntakePosition, ElevatorSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(pulleyIntakePosition, PulleySubsystem.getInstance()));
    //commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().doIntake();}).withTimeout(1.0));
  //commandGroup.addCommands(new PositionElbowCommand(elbowSecurePosition));
    return commandGroup;
  }
}