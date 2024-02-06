package frc.robot.controller;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;


public class MyXboxController implements TeleOpController{
    XboxController xboxController;

    public MyXboxController(int port) {
        xboxController = new XboxController(port);
    }

    @Override
    public Trigger moveTrigger() {
        return xboxController.leftTrigger(CommandScheduler.getInstance().getDefaultButtonLoop()).castTo(Trigger::new);
    }

    @Override
    public Trigger releaseToAMPTrigger() {
        return xboxController.a(CommandScheduler.getInstance().getDefaultButtonLoop()).castTo(Trigger::new);
    }

    @Override
    public Trigger intakeTrigger() {
        return xboxController.b(CommandScheduler.getInstance().getDefaultButtonLoop()).castTo(Trigger::new);
    }

    @Override
    public Trigger releaseToShooterTrigger() {
        return xboxController.x(CommandScheduler.getInstance().getDefaultButtonLoop()).castTo(Trigger::new);
    }

    @Override
    public double getXSpeed() {
        
        return xboxController.getLeftX();
    }

    @Override
    public double getYSpeed() {
        return xboxController.getLeftY();
    }

    @Override
    public Trigger raiseArmTrigger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Trigger lowerArmTrigger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getRaiseSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getLowerSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getRotation() {
        // TODO Auto-generated method stub
        return xboxController.getRightX();
        ///return 0;
    }

    @Override
    public double getElbowSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

}


