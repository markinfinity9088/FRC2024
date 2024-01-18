package frc.robot.utils;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

/**
 * A command that runs the swerve drive for a specified amount of time
 *
 */
public class SwerveDriveForDurationCommand extends Command {
  protected Timer m_timer = new Timer();
  private final double m_duration;
  private double xspeed, yspeed, rotation;

  /**
   * Creates a new SwerveDriveCommand. This command run the swerve drive, and end after the specified duration.
   *
   * @param seconds the time to run, in seconds
   */
  public SwerveDriveForDurationCommand(int seconds, double xspeed, double yspeed, double rotation) {
    m_duration = seconds;
    SendableRegistry.setName(this, getName() + ": " + seconds + " seconds");
    this.xspeed = xspeed;
    this.yspeed = yspeed;
    this.rotation = rotation;
    addRequirements(SwerveDriveSubsystem.getInstance());
  }

  @Override
  public void initialize() {
    m_timer.restart();
  }

  @Override
  public void execute() {
    System.out.println("Executing "+getName());
    SwerveDriveSubsystem.getInstance().drive(xspeed, yspeed, rotation,true, true);
  }

  @Override
  public void end(boolean interrupted) {
    m_timer.stop();
  }

  @Override
  public boolean isFinished() {
    return m_timer.hasElapsed(m_duration);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
