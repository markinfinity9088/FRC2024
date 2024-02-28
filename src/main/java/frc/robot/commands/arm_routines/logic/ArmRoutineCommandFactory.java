package frc.robot.commands.arm_routines.logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.HoldSubsystemInPositionCommand;
import frc.robot.commands.PositionSubsystemCommand;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.WristSubsystem;

/*
 * Purpose of this class is to create commands that complete a defined Arm routine
 * it uses array of initalPositions array to first do sequential move commands followed by
 * final hold position to do final hold of all the arms components
 */
public class ArmRoutineCommandFactory {

    private enum LocalActionTypes {
        UseMove,
        UseHold
    };

    private static ArmRoutineCommandFactory instance=null;

    private ArmRoutineCommandFactory() {}

    public static ArmRoutineCommandFactory getInstance() {
        if (instance == null) {
            instance = new ArmRoutineCommandFactory();
        }
        return instance;
    }
    
    public Command executeArmRoutine(ArmRoutine routine) {

        SequentialCommandGroup commandSequence = new SequentialCommandGroup();
        
        //Iterate over initial sequence of positions and add to sequential group of move commands
        //This is helpful if we want to add some sequence instead of moving all pieces of arm together, like move wrist a bit first before we start moving arm .
        for (ArmPositioningInfo positionInfo : routine.getPreHoldSequences()) {
            Command command = getParallelArmPositionGroup(positionInfo, LocalActionTypes.UseMove);
            if (command != null) {
                commandSequence.addCommands(command);
            }
        }

        //Add commands for final hold positions for all the arm pieces
        ArmPositioningInfo positioningInfo = routine.getHoldPosition();
        Command command = getParallelArmPositionGroup(positioningInfo, LocalActionTypes.UseHold);
        if(command != null) {
            commandSequence.addCommands(command);
        }

        return commandSequence;
    }


    //creates parallel set of commands that executes move or hold action on the arm components (wrist, elbow, elevator)
    //actiontype tells whether to use move or hold
    private Command getParallelArmPositionGroup(ArmPositioningInfo positionInfo, LocalActionTypes actionType) {
        ParallelCommandGroup parallelGroup = new ParallelCommandGroup();
        boolean added = false;

        if (positionInfo.getElbowPosition() != null) {
            long positionValue = positionInfo.getElbowPosition();
            Command command = (actionType==LocalActionTypes.UseMove)? new PositionSubsystemCommand(positionValue, ElbowSubsystem.getInstance()) : 
                                        new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance(), positionValue);
            parallelGroup.addCommands(command);
            added = true;
        }

        if (positionInfo.getWristPosition() != null) {
            long positionValue = positionInfo.getWristPosition();

            Command command = (actionType==LocalActionTypes.UseMove)? new PositionSubsystemCommand(positionValue, WristSubsystem.getInstance()) : 
                                        new HoldSubsystemInPositionCommand(WristSubsystem.getInstance(), positionValue);
            parallelGroup.addCommands(command);
            added = true;
        }

        if (positionInfo.getElevatorPosition() != null) {
            long positionValue = positionInfo.getElevatorPosition();
            Command command = (actionType==LocalActionTypes.UseMove)? new PositionSubsystemCommand(positionValue, ElevatorSubsystem.getInstance()) : 
                                        new HoldSubsystemInPositionCommand(ElevatorSubsystem.getInstance(), positionValue);
            parallelGroup.addCommands(command);
            added = true;
        }
        
        if (!added) {
            return null;
        }

        return parallelGroup;
       
    }

    

}
