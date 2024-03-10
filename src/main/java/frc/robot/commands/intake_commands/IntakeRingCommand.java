package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubSystem;

public class IntakeRingCommand extends Command{
    boolean m_useSensor;

    public IntakeRingCommand(boolean useSensor) {
        m_useSensor = useSensor;
        addRequirements(IntakeSubSystem.getInstance());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        IntakeSubSystem.getInstance().doIntake();
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubSystem.getInstance().stop();
    }

    @Override
    public boolean isFinished() {
        if (m_useSensor) {
            return IntakeSubSystem.getInstance().isRingDetected();
        }
        return false;
    }
    

}
