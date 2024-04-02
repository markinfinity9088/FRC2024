package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface TeleOpController {
    //Bindings for button toggle
    Trigger intakeTrigger(); // Release take ring from floor
    Trigger intakeTriggerDrive();
    Trigger releaseToAMPTrigger(); // Release ring to AMP
    Trigger getShootTrigger(); // Shoot the ring out


    Trigger getHookUpTrigger();
    Trigger getHookDownTrigger();
    Trigger getElbowTrigger();
    Trigger getWristTrigger();
    Trigger getElevatorTrigger();
    Trigger getPivotTriggerUp();
    Trigger getPivotTriggerDown();
    Trigger getPivotPresetTrigger();

    // Bindings for Axis value
    double getWristSpeed();
    double getElbowSpeed();
    double getElevatorSpeed();
    double getHookUpSpeed();
    double getHookDownSpeed();

    Trigger holdElbowInPositionTrigger();
    Trigger holdWristInPositionTrigger();

    // Drive Bindings
    Trigger moveTrigger();
    double getXSpeedSwerve();
    double getYSpeedSwerve();
    double getRotation();

    

    //Cancel all commands trigger
    Trigger cancelAllCommandsTrigger();

    //Preset Triggers
    Trigger resetLastKnownPresetNameTrigger();
    //Trigger presetPrimaryTrigger(); // This button can be used as master trigger for preset while other buttons are to choose trigger
    Trigger pickupPresetTrigger();
    Trigger stowPresetTrigger();
    Trigger handoffPresetTrigger();
    Trigger ampPresetTrigger();
    Trigger ampScorePresetTrigger();
    Trigger getTestTrigger();

   Trigger getResetTrigger();


    //Independent Hook triggers
    Trigger getLeftHookTrigger();
    Trigger getRightHookTrigger();

    public Trigger executeAmpDriveAndPositionPreset() ; //drive to amp and position arm


    //Extra drive routine features helpful in case of radio lags to do some delta controls instead of joystic based swerve
    Trigger additionalDrive1Trigger();
    Trigger additionalDrive2Trigger();

    Trigger slowDownSwerveTrigger();
    Trigger speedUpSwerveTrigger();

    Trigger driveFrontSomeDistanceTrigger();
    Trigger driveBackSomeDistanceTrigger();
    Trigger driveLeftSomeDistanceTrigger();
    Trigger driveRightSomeDistancTrigger();
    Trigger rotate360DriveForwardTrigger();

}