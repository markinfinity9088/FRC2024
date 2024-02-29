package frc.robot.commands.autonCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

//Simple command to move out of alliance area at start in auto mode...
public class SimpleMoveOutCommand extends Command {

    SimpleMoveOutCommand() {
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
