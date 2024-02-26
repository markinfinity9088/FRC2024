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
}