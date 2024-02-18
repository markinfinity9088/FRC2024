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
        xboxController1 = new CommandXboxController(Constants.OIConstants.kDriverControllerPort);
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
    public Trigger releaseToShooterTrigger() {
        return xboxController1.x();
    }

    @Override
    public double getXSpeed() {
        return xboxController1.getLeftX();
    }

    @Override
    public double getYSpeed() {
        return xboxController1.getLeftY();
    }

    @Override
    public Trigger hookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hookTrigger'");
    }

    @Override
    public Trigger raiseHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'raiseHookTrigger'");
    }

    @Override
    public Trigger lowerHookTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lowerHookTrigger'");
    }

    @Override
    public double getElbowSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElbowSpeed'");
    }

    @Override
    public double getPivotspeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotspeed'");
    }

    @Override
    public double getElevatorSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElevatorSpeed'");
    }

    @Override
    public double getHookRaiseSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookRaiseSpeed'");
    }

    @Override
    public double getHookLowerSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHookLowerSpeed'");
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
    public Trigger getElevatorTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElevatorTrigger'");
    }

    @Override
    public Trigger getPivotTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPivotTrigger'");
    }

    @Override
    public double getWristSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWristSpeed'");
    } 

}


