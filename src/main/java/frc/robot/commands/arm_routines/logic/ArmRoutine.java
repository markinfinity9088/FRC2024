package frc.robot.commands.arm_routines.logic;

//Routine class that holds data used by RoutineCommandFactory to create commands accordingly
public class ArmRoutine {

    //holds initial set of move positions, each item in array will be executed sequentiall while all position within a array item will be parallel
    private ArmPositioningInfo m_initialPositions[]; 
    //This is final position for hold, all positions are executed parallely
    private ArmPositioningInfo m_finalArmPositioningInfo;

    public ArmRoutine(ArmPositioningInfo initialPositions[], ArmPositioningInfo finalPosition) {
        m_initialPositions = initialPositions;
        m_finalArmPositioningInfo = finalPosition;
    }

    public ArmPositioningInfo[] getPreHoldSequences() {
         return m_initialPositions;
    }

    public ArmPositioningInfo getHoldPosition() {
        return m_finalArmPositioningInfo;
    }

}
