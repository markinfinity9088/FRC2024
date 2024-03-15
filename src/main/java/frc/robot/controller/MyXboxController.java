package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.XboxController;

public class MyXboxController implements TeleOpController{
    XboxController xboxController;
    CommandXboxController xboxController1;
    static private MyXboxController self;

    private MyXboxController() {
        xboxController1 = new CommandXboxController(Constants.OIConstants.kDriverControllerPort1);
    }

    static public TeleOpController getInstance() {
        if (self==null) self = new MyXboxController();
        return self;
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        return xboxController1.a();
    }

    @Override
    public Trigger intakeTrigger() {
        return xboxController1.b();
    }

    @Override
    public Trigger getShootTrigger() {
        return xboxController1.x();
    }

    @Override
    public double getXSpeedSwerve() {
        return xboxController1.getLeftX();
    }

    @Override
    public double getYSpeedSwerve() {
        return xboxController1.getLeftY();
    }

    @Override
    public Trigger getHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'raiseHookTrigger'");
    }

    @Override
    public double getIntakeSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIntakeSpeed'");
    }

    @Override
    public double getElbowSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElbowSpeed'");
    }

    @Override
    public Trigger getResetTrigger() {
        throw new UnsupportedOperationException("Unimplemented method 'getIntakeSpeed'");
    }

    @Override
    public double getPivotSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotspeed'");
    }

    @Override
    public double getElevatorSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElevatorSpeed'");
    }

    @Override
    public double getHookSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookRaiseSpeed'");
    }

    @Override
    public double getRotation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRotation'");
    }

    @Override
    public Trigger getElbowTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElbowTrigger'");
    }

    @Override
    public Trigger getWristTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWristTrigger'");
    }

    @Override
    public Trigger intakeTriggerDrive(){
        throw new UnsupportedOperationException("Unimplemented method 'intakeTrigger'");
    }

    @Override
    public Trigger getElevatorTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElevatorTrigger'");
    }

    @Override
    public Trigger getPivotTriggerUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotTrigger'");
    }

    @Override
    public Trigger getPivotTriggerDown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotTrigger'");
    }

    @Override
    public double getWristSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWristSpeed'");
    } 

    @Override
    public Trigger swerveTrigger() {
           throw new UnsupportedOperationException("Unimplemented method 'getWristSpeed'");
    }

    @Override
    public Trigger moveTrigger() {
        throw new UnsupportedOperationException("Unimplemented method 'moveTrigger'");
    }

    @Override
    public Trigger holdElbowInPositionTrigger() {
        throw new UnsupportedOperationException("Unimplemented method 'holdElbowInPositionTrigger'");
    }

    @Override
    public Trigger holdWristInPositionTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'holdWristInPositionTrigger'");
    }

    @Override
    public Trigger getHookUpTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookUpTrigger'");
    }

    @Override
    public Trigger getHookDownTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookDownTrigger'");
    }

    @Override
    public double getHookUpSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookUpSpeed'");
    }

    @Override
    public double getHookDownSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookDownSpeed'");
    }

    @Override
    public Trigger moveWristTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveWristTrigger'");
    }

    @Override
    public Trigger cancelAllCommandsTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveWristTrigger'");
    }

    @Override
    public Trigger pickupPresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pickupPresetTrigger'");
    }

    @Override
    public Trigger stowPresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stowPresetTrigger'");
    }

    @Override
    public Trigger handoffPresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handoffPresetTrigger'");
    }

    @Override
    public Trigger ampPresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ampPresetTrigger'");
    }

    @Override
    public Trigger presetPrimaryTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'presetPrimaryTrigger'");
    }

    @Override
    public Trigger resetLastKnownPresetNameTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetLastKnownPresetNameTrigger'");
    }

    @Override
    public Trigger slowMaxSpeedTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'slowMaxSpeedTrigger'");
    }

    @Override
    public Trigger getPivotPresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotPresetTrigger'");
    }

    @Override
    public Trigger executeAmpDriveAndPositionPreset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeAmpDriveAndPositionPreset'");
    }

    @Override
    public Trigger getTestTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTestTrigger'");
    } 
}


