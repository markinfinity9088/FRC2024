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
    final double maxSpeed = 0.5;

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
        return ps4Controller2.R1();
    }
    
    @Override
    public Trigger getShootTrigger() {
        return ps4Controller1.R2();
    }

    @Override
    public Trigger intakeTrigger() {
        return ps4Controller2.L1();
    }

    @Override
    public Trigger swerveTrigger() {
        return ps4Controller1.L1();
    }

    @Override
    public double getXSpeedSwerve() {
        double speed = ps4Controller1.getLeftX();
        if (speed>maxSpeed)
            speed=maxSpeed;
        else if (speed<-maxSpeed)
            speed=-maxSpeed;
        //System.out.println("xspeed:"+leftx);
        return MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getYSpeedSwerve() {
        double speed = ps4Controller1.getLeftY();
        if (speed>maxSpeed)
            speed=maxSpeed;
        else if (speed<-maxSpeed)
            speed=-maxSpeed;
        return MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getRotation() {
        return MathUtil.applyDeadband(ps4Controller1.getRightX(), OIConstants.kDriveDeadband);
    }

    @Override
    public Trigger getHookTrigger() {
        return ps4Controller1.R1();
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
        return MathUtil.applyDeadband(ps4Controller2.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getPivotspeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElevatorSpeed() {
        return MathUtil.applyDeadband(ps4Controller2.getR2Axis() - ps4Controller2.getL2Axis(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getHookSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public Trigger getElbowTrigger() {
        return ps4Controller2.circle();
    }

    @Override
    public Trigger getWristTrigger() {
        return ps4Controller2.square();
    }

    @Override
    public Trigger getElevatorTrigger() {
        return ps4Controller2.triangle();
    }

    @Override
    public Trigger getPivotTrigger() {
        return ps4Controller2.cross();
    }
}