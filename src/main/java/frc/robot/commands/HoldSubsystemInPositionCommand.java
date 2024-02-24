package frc.robot.commands;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PositionableSubsystem;

/**
 * A command that performs the intake
 *
 */
public class HoldSubsystemInPositionCommand extends Command {
  private long position; // Encoder value for subsystem to be positioned
  private PositionableSubsystem subsystem;

  /**
   * Creates a new command to be in poistion
   *
   * @param seconds the time to run, in seconds
   */
  public HoldSubsystemInPositionCommand(SubsystemBase subsystem) {
    this.subsystem = (PositionableSubsystem) subsystem;
    SendableRegistry.setName(this, getName());
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    position = ((PositionableSubsystem)subsystem).getPosition();
    System.out.println("Hold Position "+subsystem.getName()+" command initialized with pos:"+position);
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
    return false; //subsystem.isAtPosition(position);
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}