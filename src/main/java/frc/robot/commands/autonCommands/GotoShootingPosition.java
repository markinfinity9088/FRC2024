package frc.robot.commands.autonCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class GotoShootingPosition extends Command {
    GotoShootingPosition() {
        SwerveDriveSubsystem swerveDriveSubsystem = SwerveDriveSubsystem.getInstance();
        addRequirements(swerveDriveSubsystem);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }
}
