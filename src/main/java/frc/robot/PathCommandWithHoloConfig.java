package frc.robot;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;

import edu.wpi.first.wpilibj2.command.Command;

public class PathCommandWithHoloConfig {
    private final HolonomicPathFollowerConfig m_holoConfig;
    private Command m_command;

    public PathCommandWithHoloConfig(Command pathCommand, HolonomicPathFollowerConfig holoConfig) {
        m_holoConfig = holoConfig;
        m_command = pathCommand;
    }

    HolonomicPathFollowerConfig getHoloConfig() {
        return m_holoConfig;
    }

    Command getCommand() {
        return m_command;
    }
}