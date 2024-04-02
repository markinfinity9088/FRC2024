package frc.robot.controller;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Constants.OIConstants;

public class PS4ControllerSingle implements TeleOpController {
    // The driver's controller
    private CommandPS4Controller ps4Controller1;
    static private PS4ControllerSingle self;
    final double maxSpeed = 0.5;

    private PS4ControllerSingle() {
        ps4Controller1 = new CommandPS4Controller(Constants.OIConstants.kDriverControllerPort0);
    }

    static public TeleOpController getInstance() {
        if (self==null) self = new PS4ControllerSingle();
        return self;
    }

    @Override
    public Trigger getResetTrigger() {
        return ps4Controller1.square();
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        return ps4Controller1.R1();
    }
    
    @Override
    public Trigger getShootTrigger() {
        return ps4Controller1.L2();
    }

    @Override
    public Trigger intakeTrigger() {
        return ps4Controller1.L1();
    }

    @Override
    public Trigger intakeTriggerDrive() {
        return ps4Controller1.L1();
    }    


    @Override
    public double getXSpeedSwerve() {
        double speed = ps4Controller1.getLeftX();
        speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
        //System.out.println("xspeed:"+leftx);
        return -MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getYSpeedSwerve() {
        double speed = ps4Controller1.getLeftY();
        speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
        return -MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getRotation() {
        return -MathUtil.applyDeadband(ps4Controller1.getRightX(), OIConstants.kDriveDeadband);
    }



    @Override
    public double getWristSpeed() {
        return MathUtil.applyDeadband(-ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElbowSpeed() {
        return MathUtil.applyDeadband(-ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }


    @Override
    public double getElevatorSpeed() {
        return MathUtil.applyDeadband(-ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }


    @Override
    public Trigger getElbowTrigger() {
        return ps4Controller1.circle();
    }

    @Override
    public Trigger getWristTrigger() {
        return ps4Controller1.square();
    }

    @Override
    public Trigger getElevatorTrigger() {
        return ps4Controller1.triangle();
    }

    @Override
    public Trigger getPivotTriggerUp() {
        return ps4Controller1.L1();
    }

    @Override
    public Trigger getPivotTriggerDown() {
        return ps4Controller1.R1();
    }

    @Override
    public Trigger moveTrigger() {
        return ps4Controller1.R2();
    }

    @Override
    public Trigger holdWristInPositionTrigger() {
        return ps4Controller1.PS();
    }

    @Override
    public Trigger holdElbowInPositionTrigger() {
        return ps4Controller1.PS();
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
    public Trigger cancelAllCommandsTrigger() {
        return ps4Controller1.touchpad();
        
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

    @Override
    public Trigger getLeftHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLeftHookTrigger'");
    }

    @Override
    public Trigger getRightHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRightHookTrigger'");
    }

    @Override
    public Trigger ampScorePresetTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ampScorePresetTrigger'");
    }
}