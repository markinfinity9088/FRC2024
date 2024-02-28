package frc.robot.commands;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.PositionableSubsystem;

/**
 * A command that performs the intake
 *
 */
public class PositionSubsystemCommand extends Command {
  private long position; // Encoder value for subsystem to be positioned
  private PositionableSubsystem subsystem;
  private long tolerance = 5;

  /**
   * Creates a new command to be in poistion
   *
   * @param seconds the time to run, in seconds
   */
  public PositionSubsystemCommand(long position, SubsystemBase subsystem) {
    this.position = position;
    this.subsystem = (PositionableSubsystem) subsystem;
    SendableRegistry.setName(this, getName());
    
    addRequirements(subsystem);
  }

  public PositionSubsystemCommand(long position, SubsystemBase subsystem, Long tolerance) {
    this(position, subsystem);
    this.tolerance = tolerance;
  }

  @Override
  public void initialize() {
    System.out.println("Position "+subsystem.getName()+" command initialized");
  }

  @Override
  public void execute() {
    //System.out.println("Executing "+getName());
    subsystem.moveToPosition(position);
  }

  @Override
  public void end(boolean interrupted) {
    subsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return subsystem.isAtPosition(position, tolerance);
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}