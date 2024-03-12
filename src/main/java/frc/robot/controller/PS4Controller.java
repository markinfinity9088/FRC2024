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
        return presetPrimaryTrigger().negate().and(ps4Controller2.R1()) ;
    }
    
    @Override
    public Trigger getShootTrigger() {
        return ps4Controller1.R1();
    }

    @Override
    public Trigger intakeTrigger() {
        //L1 is pressed and the preset trigger is not pressed
        return presetPrimaryTrigger().negate().and( ps4Controller2.L1());
    }

    @Override
    public Trigger intakeTriggerDrive(){
        return ps4Controller1.L1();
    }

    @Override
    public Trigger swerveTrigger() {
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
        return ps4Controller1.square();
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
    public double getIntakeSpeed() {
        // int intakeSpeed = 1;
        // if (ps4Controller2.L1().whil){
        //     return intakeSpeed;
        // } else if (ps4Controller2.R1()){
        //     return -intakeSpeed;
        // }
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
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
    public double getPivotSpeed() {
        return MathUtil.applyDeadband(-ps4Controller2.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElevatorSpeed() {
        return MathUtil.applyDeadband(ps4Controller2.getR2Axis() - ps4Controller2.getL2Axis(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getHookSpeed() {
        return MathUtil.applyDeadband(-ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
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
        return ps4Controller1.triangle().or(ps4Controller2.povUp());
    }

    @Override
    public Trigger getPivotTriggerDown() {
        return ps4Controller1.cross().or(ps4Controller2.povDown());
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
    public Trigger getHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookTrigger'");
    }

    @Override
    public Trigger moveWristTrigger() {
        return ps4Controller1.circle();
    }

    @Override
    public Trigger cancelAllCommandsTrigger() {
        return ps4Controller2.touchpad();
        
    }


    ///Presets
     @Override
    public Trigger presetPrimaryTrigger() {
         return ps4Controller2.cross();
    }

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
        return ps4Controller2.triangle();
    }

    @Override
    public Trigger resetLastKnownPresetNameTrigger() {
        return ps4Controller2.circle();
    }

    @Override
    public Trigger slowMaxSpeedTrigger() {
        return ps4Controller1.circle();
    }

    // @Override
    // public Trigger pivotPresetUpTrigger(){
    //     return ps4Controller1.povUp();
    // }

    // public Trigger pivotPresetDownTrigger(){
    //     return ps4Controller1.povUp();
    // }


    //
    
   

}