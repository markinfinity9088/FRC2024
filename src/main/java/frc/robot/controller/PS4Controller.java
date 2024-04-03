package frc.robot.controller;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Constants.OIConstants;

public class PS4Controller implements TeleOpController {
    // The driver's controller
    private CommandPS4Controller ps4Controller1;
    private CommandPS4Controller ps4Controller2;
    static private PS4Controller self;
    final double maxSpeed = 1;

    private PS4Controller() {
        ps4Controller1 = new CommandPS4Controller(Constants.OIConstants.kDriverControllerPort0);
        ps4Controller2 = new CommandPS4Controller(Constants.OIConstants.kDriverControllerPort1);

    }

    static public TeleOpController getInstance() {
        if (self==null) self = new PS4Controller();
        return self;
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        //activate this only when preset trigger is not pressed and R1 is pressed
        return ps4Controller2.R1() ;
    }
    
    @Override
    public Trigger getShootTrigger() {
        return ps4Controller1.R1();
    }

    @Override
    public Trigger intakeTrigger() {
        //L1 is pressed and the preset trigger is not pressed
        return  ps4Controller2.L1();
    }

    @Override
    public Trigger intakeTriggerDrive(){
        return ps4Controller1.L1();
    }


    @Override
    public double getXSpeedSwerve() {
        double speed = -ps4Controller1.getLeftX();
        speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
        //System.out.println("xspeed:"+leftx);
        return MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getYSpeedSwerve() {
        double speed = -ps4Controller1.getLeftY();
        speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
        return MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getRotation() {
        return -MathUtil.applyDeadband(ps4Controller1.getRightX(), OIConstants.kDriveDeadband);
    }

    @Override
    public Trigger getResetTrigger() {
        return additionalDriveTriggerPressedTrigger().negate().and( ps4Controller1.square() );
    }

    @Override
    public Trigger getPivotPresetTrigger() {
        return  additionalDriveTriggerPressedTrigger().negate().and(ps4Controller1.circle());
    }

    @Override
    public Trigger getHookUpTrigger() {
        return ps4Controller1.L2();
    }

    @Override
    public double getHookUpSpeed() {
        return (ps4Controller1.getL2Axis() + 1) / 2;
    }

    @Override
    public double getHookDownSpeed() {
        return -(ps4Controller1.getR2Axis() + 1) / 2;
    }
    

    @Override
    public Trigger getHookDownTrigger() {
        return ps4Controller1.R2();
    }


    @Override
    public double getWristSpeed() {
        return MathUtil.applyDeadband(-ps4Controller2.getRightY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElbowSpeed() {
        return MathUtil.applyDeadband(-ps4Controller2.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElevatorSpeed() {
        return MathUtil.applyDeadband(ps4Controller2.getR2Axis() - ps4Controller2.getL2Axis(), OIConstants.kDriveDeadband);
    }

    private double getLeftY(CommandPS4Controller controller) {
        double val = controller.getLeftY();
        return Math.abs(val) > OIConstants.kDriveDeadband ? val:0.0;
    }

    private double getRightY(CommandPS4Controller controller) {
        double val = controller.getRightY();
        return Math.abs(val) > OIConstants.kDriveDeadband ? val:0.0;
    }

    @Override
    public Trigger getElbowTrigger() {
        return new Trigger(()->(getLeftY(ps4Controller2)!=0.0));
    }

    @Override
    public Trigger getWristTrigger() {
        return new Trigger(()->(getRightY(ps4Controller2)!=0.0));
    }

    @Override
    public Trigger getElevatorTrigger() {
        return new Trigger(() -> (getElevatorSpeed()!=0));
        // return ps4Controller2.triangle();
        //hacky way not to set elevator speed from R2 buttons when we are trying to activate presets as preset overrides R2
        //return presetPrimaryTrigger().negate();
    }

    @Override
    public Trigger getPivotTriggerUp() {
        Trigger ps1trigger = additionalDriveTriggerPressedTrigger().negate().and(ps4Controller1.triangle());
        return ps1trigger.or(ps4Controller2.povUp());
    }

    @Override
    public Trigger getPivotTriggerDown() {
        Trigger ps1Trigger = additionalDriveTriggerPressedTrigger().negate().and(ps4Controller1.cross());
        return ps1Trigger.or(ps4Controller2.povDown());
    }

    @Override
    public Trigger moveTrigger() {
        return ps4Controller1.R2();
    }

    @Override
    public Trigger holdWristInPositionTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookTrigger'");
    }

    @Override
    public Trigger holdElbowInPositionTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookTrigger'");
    }

   
    @Override
    public Trigger cancelAllCommandsTrigger() {
        Trigger ps1Trigger = additionalDriveTriggerPressedTrigger().negate().and(ps4Controller1.touchpad());
        return ps1Trigger.or(ps4Controller2.touchpad());
        
    }


    ///Presets
   /*  @Override
    public Trigger presetPrimaryTrigger() {
         return ps4Controller2.cross();
    }
    */

    @Override
    public Trigger pickupPresetTrigger() {
        // return presetPrimaryTrigger().and(ps4Controller2.L1());
        return ps4Controller2.cross();
    }

    @Override
    public Trigger stowPresetTrigger() {
        // return presetPrimaryTrigger().and(ps4Controller2.L2());
        return ps4Controller2.square();
    }

    @Override
    public Trigger handoffPresetTrigger() {
        // return presetPrimaryTrigger().and(ps4Controller2.R1());
        return ps4Controller2.circle();
    }

    @Override
    public Trigger ampPresetTrigger() {
        // return presetPrimaryTrigger().and(ps4Controller2.R2());
        return ps4Controller2.povLeft();
    }

    @Override
    public Trigger ampScorePresetTrigger() {
        // return presetPrimaryTrigger().and(ps4Controller2.R2());
        return ps4Controller2.povRight();
    }

    @Override
    public Trigger resetLastKnownPresetNameTrigger() {
        return ps4Controller2.circle();
    }

    @Override
    public Trigger executeAmpDriveAndPositionPreset() {
        return additionalDriveTriggerPressedTrigger().negate().and(ps4Controller1.touchpad());
    }

    @Override
    public Trigger getTestTrigger(){
        return ps4Controller1.povDown(); //disabled for now
    }

    @Override
    public Trigger getLeftHookTrigger() {
        return ps4Controller1.povLeft();
    }

    @Override
    public Trigger getRightHookTrigger() {
        return ps4Controller1.povRight();
    }

    //Additional drive commands helpful in extreme situations like radio lags
    @Override
    public Trigger additionalDrive1Trigger() {
        return ps4Controller1.povUp();
    }

    @Override
    public Trigger additionalDrive2Trigger() {
        return ps4Controller1.povDown();
    }

    @Override
    public Trigger slowDownSwerveTrigger() {
        return additionalDrive2Trigger().and(ps4Controller1.circle());
    }

    @Override
    public Trigger speedUpSwerveTrigger() {
        return additionalDrive2Trigger().and(ps4Controller1.square());
    }

    @Override
    public Trigger rotate360DriveForwardTrigger() {
        return additionalDrive2Trigger().and(ps4Controller1.cross());
    }

    @Override
    public Trigger driveFrontSomeDistanceTrigger() {
        return additionalDrive1Trigger().and(ps4Controller1.triangle());
    }

    @Override
    public Trigger driveBackSomeDistanceTrigger() {
        return additionalDrive1Trigger().and(ps4Controller1.cross());
    }

    @Override
    public Trigger driveLeftSomeDistanceTrigger() {
        return additionalDrive1Trigger().and(ps4Controller1.square());
    }

    @Override
    public Trigger driveRightSomeDistancTrigger() {
        return additionalDrive1Trigger().and(ps4Controller1.circle());
    }

    

   private Trigger additionalDriveTriggerPressedTrigger() {
        Trigger additionalDriveButtonTriggers = additionalDrive1Trigger().or(additionalDrive2Trigger());
        return additionalDriveButtonTriggers;
   }
    
   

}