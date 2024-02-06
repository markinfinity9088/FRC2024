package frc.robot.commands;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElbowSubsystem;

/**
 * A command that performs the intake
 *
 */
public class PositionElbowForIntakeCommand extends Command {
  private double intakePosition = 2.0; // Encoder value for elbow to be positioned for intake
  private final ElbowSubsystem elbow = ElbowSubsystem.getInstance();
  /**
   * Creates a new command to be in poistion for elbow
   *
   * @param seconds the time to run, in seconds
   */
  public PositionElbowForIntakeCommand() {
    SendableRegistry.setName(this, getName());
    addRequirements(ElbowSubsystem.getInstance());
  }

  @Override
  public void initialize() {
    System.out.println("PositionElbowForIntake initialized");
  }

  @Override
  public void execute() {
    System.out.println("Executing "+getName());
    elbow.moveToPosition(intakePosition);
  }

  @Override
  public void end(boolean interrupted) {
    elbow.stop();
  }

  @Override
  public boolean isFinished() {
    return elbow.isAtPosition(intakePosition);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}