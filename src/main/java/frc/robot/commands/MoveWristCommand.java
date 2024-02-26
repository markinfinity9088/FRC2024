package frc.robot.commands;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.utils.PID.AsymmetricProfiledPIDController;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.Constraints;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.State;


public class MoveWristCommand extends Command{
    private WristSubsystem wrist;
    private boolean up;
    private double position;
   
    public static final Constraints profileConstraints = new Constraints(1, 0.2, 0.5);
    private AsymmetricProfiledPIDController wristPIDController = 
      new AsymmetricProfiledPIDController(8.0,0,0.01, profileConstraints); //MUST START AT 0 P

    TrapezoidProfile trapezoidProfile = new TrapezoidProfile(new TrapezoidProfile.Constraints(5, 10));
  
    public MoveWristCommand(double position, boolean up){
        wrist = WristSubsystem.getInstance();
        this.up = up;
        this.position = position;
    }
    
  @Override
  public void initialize() {
    // Creates a new state with a position of 5 meters
    // and a velocity of 0 meters per second
    TrapezoidProfile.State pidState = new TrapezoidProfile.State(position, 0);
    
     wristPIDController.setGoal(position);
    System.out.println("Position "+wrist.getName()+" command initialized with position "+position);
  }

  @Override
  public void execute() {
    System.out.println("Executing "+getName()+" position = "+wrist.getPosition());
    double speed = wristPIDController.calculate(wrist.getPosition());
    System.out.println("Wrist speed from PID = "+speed);
    //wrist.move(speed);
    
    /*double speed = 0.4;
    if (!up){speed *= -1;}
    wrist.move(speed);
    */
  }

  @Override
  public void end(boolean interrupted) {
    wrist.stop();
  }

  @Override
  public boolean isFinished() {
    
    if (up){
        if (wrist.getPosition() >= position){
            System.out.println("Wrist command up finished "+wrist.getPosition()+" for given position"+position);
            return true;
        }
    } else {
        if (wrist.getPosition() <= position){
                      System.out.println("Wrist command down finished "+wrist.getPosition()+" for given position"+position);

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
