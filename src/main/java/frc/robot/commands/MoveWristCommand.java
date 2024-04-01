package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WristSubsystem;

public class MoveWristCommand extends Command{
    private WristSubsystem wrist;
    private boolean up;
    private double position;
    public MoveWristCommand(double position, boolean up){
        wrist = WristSubsystem.getInstance();
        this.up = up;
        this.position = position;
    }
    
  @Override
  public void initialize() {
    // System.out.println("Position "+wrist.getName()+" command initialized");
  }

  @Override
  public void execute() {
    //System.out.println("Executing "+getName());
    double speed = 0.4;
    if (!up){speed *= -1;}
    wrist.move(speed);
  }

  @Override
  public void end(boolean interrupted) {
    wrist.stop();
  }

  @Override
  public boolean isFinished() {
    
    if (up){
        if (wrist.getPosition() >= position){
            // System.out.println("Wrist command up finished "+wrist.getPosition()+" for given position"+position);
            return true;
        }
    } else {
        if (wrist.getPosition() <= position){
                      // System.out.println("Wrist command down finished "+wrist.getPosition()+" for given position"+position);

            return true;
        }
    }
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}
