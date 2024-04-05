package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GeneralConstants;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.vision.limelight.LimeLightFacade;

public class IntakeRingCommand extends Command{
    boolean m_useSensor;

    public IntakeRingCommand(boolean useSensor) {
        m_useSensor = useSensor;
        addRequirements(IntakeSubSystem.getInstance());
    }

    @Override
    public void initialize() {
        if (GeneralConstants.kUseLimeLightToIndicateRing) {
            LimeLightFacade.getInstance().setLED(false);
        }
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
            boolean isRingDetected = IntakeSubSystem.getInstance().isRingDetected();
            if (isRingDetected && GeneralConstants.kUseLimeLightToIndicateRing) {
                LimeLightFacade.getInstance().setLED(true);
            }
            return isRingDetected;
        }
        return false;
    }
    

}
