package frc.robot.commands;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PositionalableSubsystem;

/**
 * A command that performs the intake
 *
 */
public class PositionSubsystemCommand extends Command {
  private double position; // Encoder value for subsystem to be positioned
  private PositionalableSubsystem subsystem;

  /**
   * Creates a new command to be in poistion
   *
   * @param seconds the time to run, in seconds
   */
  public PositionSubsystemCommand(double position, SubsystemBase subsystem) {
    this.position = position;
    this.subsystem = (PositionalableSubsystem) subsystem;
    SendableRegistry.setName(this, getName());
    addRequirements(ElevatorSubsystem.getInstance());
  }

  @Override
  public void initialize() {
    System.out.println("Position subsystemCommand initialized");
  }

  @Override
  public void execute() {
    System.out.println("Executing "+getName());
    subsystem.moveToPosition(position);
  }

  @Override
  public void end(boolean interrupted) {
    subsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return subsystem.isAtPosition(position);
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}