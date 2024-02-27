package frc.robot.commands;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.PositionableSubsystem;

/**
 * A command that performs the intake
 *
 */
public class HoldSubsystemInPositionCommand extends Command {
  private long position; // Encoder value for subsystem to be positioned
  private PositionableSubsystem subsystem;
  boolean holdAtCurrentPosition = true;

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

  public HoldSubsystemInPositionCommand(SubsystemBase subsystem, long holdposition) {
    this(subsystem);
    this.position = holdposition;
    holdAtCurrentPosition = false;
  }

  @Override
  public void initialize() {
    if (holdAtCurrentPosition ) {
          position = ((PositionableSubsystem)subsystem).getPosition();
    }
    CommandInterruptor.getInstance().checkIsInterruptedAndReset(subsystem.getName());
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
    boolean finished = false;
    finished = CommandInterruptor.getInstance().checkIsInterruptedAndReset(subsystem.getName());
    if (finished)
      System.out.println("Finished "+getName());
    return finished; //subsystem.isAtPosition(position);
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}