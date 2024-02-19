package frc.robot.controller;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Constants.OIConstants;

public class PS4Controller implements TeleOpController {
    // The driver's controller
    private CommandPS4Controller ps4Controller1;
    static private PS4Controller self;
    final double maxSpeed = 0.5;

    private PS4Controller() {
        ps4Controller1 = new CommandPS4Controller(Constants.OIConstants.kDriverControllerPort);
    }

    static public TeleOpController getInstance() {
        if (self==null) self = new PS4Controller();
        return self;
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        return ps4Controller1.PS();
    }

    @Override
    public Trigger getShootTrigger() {
        return ps4Controller1.R2();
    }

    @Override
    public Trigger intakeTrigger() {
        return ps4Controller1.L2();
    }

    
  



    @Override
    public Trigger swerveTrigger() {
        return ps4Controller1.L1();
    }

    @Override
    public double getXSpeed() {
        double speed = ps4Controller1.getLeftX();
        if (speed>maxSpeed)
            speed=maxSpeed;
        else if (speed<-maxSpeed)
            speed=-maxSpeed;
        //System.out.println("xspeed:"+leftx);
        return MathUtil.applyDeadband(speed, OIConstants.kDriveDeadband);
    }

    @Override
    public double getYSpeed() {
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
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getWristSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElbowSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    //get Shooter speed
    @Override
    public double getShooterSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    //get trigger speed
     @Override
    public double getTiltSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }


    @Override
    public double getPivotspeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getElevatorSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public double getHookSpeed() {
        return MathUtil.applyDeadband(ps4Controller1.getLeftY(), OIConstants.kDriveDeadband);
    }

    @Override
    public Trigger getElbowTrigger() {
        return ps4Controller1.circle();
    }

    @Override
    public Trigger getShooterTrigger() {
        return ps4Controller1.cross();
    }

    @Override
    public Trigger getTiltTrigger() {
        return ps4Controller1.square();
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
    public Trigger getPivotTrigger() {
        return ps4Controller1.cross();
    }
}