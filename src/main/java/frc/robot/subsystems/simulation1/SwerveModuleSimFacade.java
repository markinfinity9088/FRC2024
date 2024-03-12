package frc.robot.subsystems.simulation1;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public class SwerveModuleSimFacade {
    //Number of rotation of motor / number of rotations of output
    //45 : 22  then 14 : 15
    private static final double kDriveMotorReduction = (45 * 22.0) / (14.0 * 15.0);
    private static final double kTurningModuleGearRatio = 9424.0 / 203.0;

    private static final double kTurningFeedforward_kv = 0.39185;
    private static final double kTurningFeedforward_ka = 0.0058658;
    private static final double kTurningFeedforward_ks = 0.35233;

    private static final double kDriveFeedforward_kv = 2.5;
    private static final double kDriveFeedforward_ka = 0.9;
    private static final double kDriveFeedforward_ks = 0.3;

    CANSparkMaxSim1 m_turningMotorSim;
    CANSparkMaxSim1 m_driveMotorSim;

    private final SwerveModuleSim m_swerveSim;
    public SwerveModuleSimFacade(CANSparkMax turningMotor, CANSparkMax drivingMotor) {
        m_swerveSim =
            new SwerveModuleSim(
                DCMotor.getNEO(1).withReduction(kDriveMotorReduction),
                kDriveFeedforward_kv,
                kDriveFeedforward_ka,
                DCMotor.getNeo550(1).withReduction(kTurningModuleGearRatio),
                kTurningFeedforward_kv
                    * 500, // Not sure why * 500, but that does stabilize it
                kTurningFeedforward_ka * 500);

        m_turningMotorSim = new CANSparkMaxSim1(turningMotor);
        m_driveMotorSim = new CANSparkMaxSim1(drivingMotor);
        
    }

    public void simulationPeriodic() {
        double vbus = RobotController.getBatteryVoltage();
        m_turningMotorSim.enable();
        m_driveMotorSim.enable();

        m_turningMotorSim.iterate(m_swerveSim.getTurningMotorVelocityRPM(), vbus, 0.02);
        m_driveMotorSim.iterate(m_swerveSim.getDriveMotorVelocityRPM(), vbus, 0.02);

        m_swerveSim.iterate(
                m_driveMotorSim.getAppliedOutput() * vbus,
                m_turningMotorSim.getAppliedOutput() * vbus,
                0.02);

        RoboRioSim.setVInVoltage(
                BatterySim.calculateDefaultBatteryLoadedVoltage(m_swerveSim.getCurrentDrawAmps()));

        m_turningMotorSim.setMotorCurrent(m_swerveSim.getTurningMotorCurrentDrawAmps());
        m_driveMotorSim.setMotorCurrent(m_swerveSim.getDriveMotorCurrentDrawAmps());
    }
}
