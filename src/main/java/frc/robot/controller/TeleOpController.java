package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface TeleOpController {
    //Bindings for button toggle
    Trigger intakeTrigger(); // Release take ring from floor
    Trigger releaseToAMPTrigger(); // Release ring to AMP
    Trigger releaseToShooterTrigger(); // Shoot the ring out
    Trigger hookTrigger(); // Raise or lower the climb hook

    Trigger raiseHookTrigger();
    Trigger lowerHookTrigger();

    Trigger getElbowTrigger();
    Trigger getWristTrigger();
    Trigger getElevatorTrigger();
    Trigger getPivotTrigger();

    // Bindings for Axis value
    double getWristSpeed();
    double getElbowSpeed();
    double getPivotspeed();
    double getElevatorSpeed();
    double getHookRaiseSpeed();
    double getHookLowerSpeed();

    // Drive Bindings
    double getXSpeed();
    double getYSpeed();
    double getRotation();
}