package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface TeleOpController {
    //Bindings for button toggle
    Trigger intakeTrigger(); // Release take ring from floor
    Trigger releaseToAMPTrigger(); // Release ring to AMP
    Trigger getShootTrigger(); // Shoot the ring out
    Trigger swerveTrigger();

    Trigger getHookTrigger();
    Trigger getElbowTrigger();
    Trigger getWristTrigger();
    Trigger getElevatorTrigger();
    Trigger getPivotTrigger();

    // Bindings for Axis value
    double getIntakeSpeed();
    double getWristSpeed();
    double getElbowSpeed();
    double getPivotspeed();
    double getElevatorSpeed();
    double getHookSpeed();
    double getShooterSpeed();
    double getTiltSpeed();
    // Drive Bindings
    double getXSpeed();
    double getYSpeed();
    double getRotation();

    //Shooter Bindings
    Trigger getShooterTrigger();
    Trigger getTiltTrigger();
    
    
}