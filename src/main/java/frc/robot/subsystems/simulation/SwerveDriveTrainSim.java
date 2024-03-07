package frc.robot.subsystems.simulation;

import edu.wpi.first.units.Time;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.MAXSwerveModule;

public class SwerveDriveTrainSim {

    GyroSim m_gyroSim;
    GyroSubsystem m_gyroSubsystem;

    MAXSwerveModule m_frontLeft;
    MAXSwerveModule m_frontRight;
    MAXSwerveModule m_backLeft;
    MAXSwerveModule m_backRight;

    private double m_prevTimeSeconds = Timer.getFPGATimestamp();
    private final double m_nominalDts = 0.02; //seconds

    public SwerveDriveTrainSim(GyroSubsystem gyroSubsystem, MAXSwerveModule frontLeft, MAXSwerveModule frontRight, 
                MAXSwerveModule backLeft, MAXSwerveModule backRight) 
    {
        m_gyroSubsystem = gyroSubsystem;
        m_gyroSim = gyroSubsystem.getGyroSim();

        m_frontLeft = frontLeft;
        m_frontRight = frontRight;
        m_backLeft = backLeft;
        m_backRight = backRight;
    }

    public void simulationInit() {
        m_frontLeft.simulationInit();
        m_frontRight.simulationInit();
        m_backLeft.simulationInit();
        m_backRight.simulationInit();
    }

    public void simulationPeriodic() {
        double currentTimeSeconds = Timer.getFPGATimestamp();
        double dtS = m_prevTimeSeconds >= 0 ? currentTimeSeconds - m_prevTimeSeconds : m_nominalDts;
        m_prevTimeSeconds = currentTimeSeconds;
        simulationPeriodic(dtS);
    }

    private void simulationPeriodic(double dtS) {
       // m_frontLeft.getSim();
    }
    
}
