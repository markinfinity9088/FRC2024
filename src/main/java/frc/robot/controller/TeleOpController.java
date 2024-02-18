package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface TeleOpController {
    //Intake Bindings
    Trigger releaseToShooterTrigger();
    Trigger intakeTrigger();
    Trigger releaseToAMPTrigger();

    double getElbowSpeed();

    // Drive Bindings
    Trigger moveTrigger();
    double getXSpeed();
    double getYSpeed();
    double getRotation();

    // Lift Bindings
    double getRaiseSpeed();
    double getLowerSpeed();
    Trigger raiseArmTrigger();
    Trigger lowerArmTrigger();
}