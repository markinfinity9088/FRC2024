package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WristSubsystem;

/** Returns a command that grabs the item */
public class IntakeCommands {
  final static long wristIntakePosition = 20;
  final static long elbowIntakePosition = 100;
  final static long elevatorIntakePosition = 50;

  final static long wristHandoffPosition = 240;
  
  final static long pivotShootPosition = 30;

  final static  long elbowSecurePosition = 250;

  final static long elbowAMPPosition = 10;
  final static long elevatorAMPPosition = 10;
  final static long pulleyAMPPosition = 10;


  public static Command sampleAutonCommand() {
    SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    commandGroup.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance(), wristHandoffPosition));
    //commandGroup.addCommands(new PositionSubsystemCommand(wristIntakePosition, WristSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(elbowIntakePosition, ElbowSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(elevatorIntakePosition, ElevatorSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(pivotShootPosition, PivotSubsystem.getInstance()));
    //commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().doIntake(1.0);}).withTimeout(1.0));
    //commandGroup.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1.0));
    return commandGroup;
  }

  public static Command sampleWristCommand() {
    SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    commandGroup.addCommands(new MoveWristCommand(970, true));
    commandGroup.addCommands(new WaitCommand(3));
    commandGroup.addCommands(new MoveWristCommand(966, false));
    return commandGroup;
  }
}