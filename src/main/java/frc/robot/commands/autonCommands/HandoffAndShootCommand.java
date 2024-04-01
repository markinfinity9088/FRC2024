package frc.robot.commands.autonCommands;

import java.util.HashMap;

import edu.wpi.first.units.Time;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.WristSubsystem;

//TODO implement state machine here to execute handoff routine, turn on shooter, once shooter reach some velocity then turn on intake(exit), stop shooter and intake
//Later we can modify to use command hierarchy instead of state machine , for now I need control 

public class HandoffAndShootCommand extends Command {
    enum HandoffAndShootState {
        Initial,
        DoingHandoff,
        DispatchingToShooter,
        StowBack,
        Completed
    }

    //Some of these values are duplicated in Presets, need to fix duplicates later
    //Adjust values later
    private static final long MAX_HANDOFF_WAIT_TIME = 10000;

    private static final long MAX_SHOOTER_TIMER = 5000;


    private long commandStartTime=0;
    private long startTimeOfCurrentState=0;
    private HandoffAndShootState currentState;

    private boolean shootProcessStarted;

    private HashMap<String, Command> currentCommands = null;

    public HandoffAndShootCommand() {
        currentState = HandoffAndShootState.Initial;
        addRequirements(ShooterSubsystem.getInstance(), WristSubsystem.getInstance(), 
                ElbowSubsystem.getInstance(), ElevatorSubsystem.getInstance(), PivotSubsystem.getInstance(),
                IntakeSubSystem.getInstance());
    }

    @Override
    public void initialize() {
        commandStartTime = 0;
        startTimeOfCurrentState = 0;
        currentCommands = new HashMap<>();
        switchState(HandoffAndShootState.Initial);
    }

    //In future, we can get rid of state machine and use command composition
    @Override
    public void execute() {
        if (commandStartTime == 0) {
            commandStartTime = System.currentTimeMillis();
        }
       

        switch (currentState) {

            case Initial:
                executeHandoff();
                startShooterWheels();
                startTiltAction();
                switchState(HandoffAndShootState.DoingHandoff);
                break;

            case DoingHandoff: 
                if (isAtHandoffPosition() && isShooterAtCorrectTilt() && isShooterAtCorrectSpeed()) {
                    switchState(HandoffAndShootState.DispatchingToShooter);
                } else if (getTimeElapsedInCurrentState() > MAX_HANDOFF_WAIT_TIME) {
                    switchState(HandoffAndShootState.DispatchingToShooter);
                }
                break;

            case DispatchingToShooter:
                if ( !shootProcessStarted ) {
                    startShootingAction();
                }
                if (getTimeElapsedInCurrentState() > MAX_SHOOTER_TIMER) {
                    switchState(HandoffAndShootState.Completed);
                }
                break;

            case StowBack:
                IntakeSubSystem.getInstance().stop();
                //to do
                break;

            case Completed:
                stopAll();
                break;
        
            default:
                break;
        }

        checkAndExecuteActiveCommands();
    }

   

    private void switchState(HandoffAndShootState newState) {
        currentState = newState;
        startTimeOfCurrentState = System.currentTimeMillis();
        // System.out.println("Current shoot state = "+ currentState);
    }

    private void startShootingAction() {
       IntakeSubSystem.getInstance().stop();
       IntakeSubSystem.getInstance().releaseToShooter();
    }

    private void startShooterWheels() {
         ShooterSubsystem.getInstance().startShooterWheels(1);
    }

    private boolean isShooterAtCorrectSpeed() {
        return (ShooterSubsystem.getInstance().getShooterVelocity() >= 0.8);
    }

    private boolean isShooterAtCorrectTilt() {
        return (PivotSubsystem.getInstance().isAtPosition(Long.valueOf(ArmPresets.PIVOT_SHOOT_POINT) , ArmPresets.PIVOT_TOLERANCE));
    }

    private boolean isAtHandoffPosition() {
        return WristSubsystem.getInstance().isAtPosition(Long.valueOf(ArmPresets.WRIST_HANDOFF_POSITION) , Long.valueOf(30));
    }

    private long getTimeElapsedInCurrentState() {
        return (System.currentTimeMillis() - startTimeOfCurrentState);
    }


    private void startTiltAction() {
        Command pivotCommand = ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt);
        pivotCommand.initialize();
        pivotCommand.execute();
        currentCommands.put("PivotCommand", pivotCommand);
    }

    

    private void executeHandoff() {
        Command handoffcommand = ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff);
        handoffcommand.initialize();
        handoffcommand.execute();
        currentCommands.put("HandoffCommand", handoffcommand);
    }

    private void checkAndExecuteActiveCommands() {
        currentCommands.forEach((key, command)-> {
            if (!command.isFinished()) {
                command.execute();
            }
        });
    }

     private void stopAll() {
        currentCommands.forEach((key, command)-> {
            if (!command.isFinished()) {
                command.end(true);
            }
        });
        
        //stop all motors
        IntakeSubSystem.getInstance().stop();
        ShooterSubsystem.getInstance().stopShooterWheels();

        if (currentState != HandoffAndShootState.Completed) {
            switchState(HandoffAndShootState.Completed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        stopAll();
    }

    @Override
    public boolean isFinished() {
        return (currentState == HandoffAndShootState.Completed);
    }
}
