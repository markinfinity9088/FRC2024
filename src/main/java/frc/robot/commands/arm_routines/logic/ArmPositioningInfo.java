package frc.robot.commands.arm_routines.logic;

//Simple Data class to hold position values of different components of Arm
public class ArmPositioningInfo {

    Long m_elbowPosition;
    Long m_wristPosition;
    Long m_elevatorPosition;
    long tolerance = 50;

    public ArmPositioningInfo(Long elbowPosition, Long wristPosition, Long elevatorPosition) {
        m_elbowPosition = elbowPosition;
        m_wristPosition = wristPosition;
        m_elevatorPosition = elevatorPosition;

    }

    public ArmPositioningInfo(Long elbowPosition, Long wristPosition, Long elevatorPosition, long tolerance) {
        this(elbowPosition,  wristPosition, elevatorPosition );
        this.tolerance = tolerance;
    }


    public Long getElbowPosition() {
        return m_elbowPosition;
    }

    public Long getWristPosition() {
        return m_wristPosition;
    }

    public Long getElevatorPosition() {
        return m_elevatorPosition;
    }

    public long getTolerance() {
        return tolerance;
    }

}
