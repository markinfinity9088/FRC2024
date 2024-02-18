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

    private PS4Controller() {
        ps4Controller1 = new CommandPS4Controller(Constants.OIConstants.kDriverControllerPort);
    }

    static public TeleOpController getInstance() {
        if (self==null) self = new PS4Controller();
        return self;
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        return ps4Controller1.R1();
    }

    @Override
    public Trigger releaseToShooterTrigger() {
        return ps4Controller1.PS();
    }

    @Override
    public Trigger intakeTrigger() {
        return ps4Controller1.L1();
    }

    @Override
    public double getXSpeed() {
        double rightx = ps4Controller1.getRightX();
        if (rightx>0.2)
            rightx=0.2;
        else if (rightx<-0.2)
            rightx=-0.2;
        //System.out.println("xspeed:"+leftx);
        return MathUtil.applyDeadband(rightx, OIConstants.kDriveDeadband);
    }

    @Override
    public double getYSpeed() {
        double righty = ps4Controller1.getRightY();
        if (righty>0.2)
            righty=0.2;
        else if (righty<-0.2)
            righty=-0.2;
        return MathUtil.applyDeadband(righty, OIConstants.kDriveDeadband);
    }

    @Override
    public double getRotation() {
        return MathUtil.applyDeadband(ps4Controller1.getRightX(), OIConstants.kDriveDeadband);
    }

    @Override
    public Trigger raiseHookTrigger() {
        return ps4Controller1.L2();
    }

    @Override
    public Trigger lowerHookTrigger() {
        return ps4Controller1.R2();
    }

    @Override
    public double getWristSpeed() {
        return ps4Controller1.getLeftX();
    }

    @Override
    public double getElbowSpeed() {
        return ps4Controller1.getLeftX();
    }

    @Override
    public double getPivotspeed() {
        return ps4Controller1.getLeftX();
    }

    @Override
    public double getElevatorSpeed() {
        return ps4Controller1.getLeftX();
    }

    @Override
    public double getHookRaiseSpeed() {
        return ps4Controller1.getL2Axis();
    }

    @Override
    public double getHookLowerSpeed() {
        return ps4Controller1.getR2Axis();
    }

    @Override
    public Trigger hookTrigger() {
        return ps4Controller1.triangle();
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
        return ps4Controller1.cross();
    }

    @Override
    public Trigger getPivotTrigger() {
        return ps4Controller1.L3();
    }
}