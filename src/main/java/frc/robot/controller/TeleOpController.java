package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface TeleOpController {
    //Bindings for button toggle
    Trigger intakeTrigger(); // Release take ring from floor
    Trigger intakeTriggerDrive();
    Trigger releaseToAMPTrigger(); // Release ring to AMP
    Trigger getShootTrigger(); // Shoot the ring out

    Trigger swerveTrigger();


    Trigger getHookUpTrigger();
    Trigger getHookDownTrigger();
    Trigger getHookTrigger();
    Trigger getElbowTrigger();
    Trigger getWristTrigger();
    Trigger getElevatorTrigger();
    Trigger getPivotTriggerUp();
    Trigger getPivotTriggerDown();
    Trigger getPivotPresetTrigger();

    // Bindings for Axis value
    double getIntakeSpeed();
    double getWristSpeed();
    double getElbowSpeed();
    double getPivotSpeed();
    double getElevatorSpeed();
    double getHookSpeed();
    double getHookUpSpeed();
    double getHookDownSpeed();

    Trigger holdElbowInPositionTrigger();
    Trigger holdWristInPositionTrigger();

    // Drive Bindings
    Trigger moveTrigger();
    double getXSpeedSwerve();
    double getYSpeedSwerve();
    double getRotation();

    //Preset Bindings
    Trigger moveWristTrigger();

    //Cancel all commands trigger
    Trigger cancelAllCommandsTrigger();

    //Preset Triggers
    Trigger resetLastKnownPresetNameTrigger();
    Trigger presetPrimaryTrigger(); // This button can be used as master trigger for preset while other buttons are to choose trigger
    Trigger pickupPresetTrigger();
    Trigger stowPresetTrigger();
    Trigger handoffPresetTrigger();
    Trigger ampPresetTrigger();
    Trigger getTestTrigger();

   Trigger getResetTrigger();


    //speed toggle
    Trigger slowMaxSpeedTrigger();

    public Trigger executeAmpDriveAndPositionPreset() ; //drive to amp and position arm
}