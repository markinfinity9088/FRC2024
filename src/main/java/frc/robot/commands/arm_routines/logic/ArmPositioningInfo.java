package frc.robot.commands.arm_routines.logic;

//Simple Data class to hold position values of different components of Arm
public class ArmPositioningInfo {

    Long m_elbowPosition;
    Long m_wristPosition;
    Long m_elevatorPosition;

    public ArmPositioningInfo(Long elbowPosition, Long wristPosition, Long elevatorPosition) {
        m_elbowPosition = elbowPosition;
        m_wristPosition = wristPosition;
        m_elevatorPosition = elevatorPosition;
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

}
