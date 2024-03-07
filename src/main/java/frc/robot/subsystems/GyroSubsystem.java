package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.simulation.GyroSim;
import frc.robot.utils.RuntimeConfig;

public class GyroSubsystem {
    // private ADXRS450_Gyro gyro1 = new ADXRS450_Gyro();
    final AHRS ahrsGyro = new AHRS(SerialPort.Port.kUSB);
    static GyroSubsystem self; // singleton
    // private AccelerometerSubsystem acc = AccelerometerSubsystem.getInstance();

    public static final String GYRO_PITCH = "Gyro Pitch";
    public static final String GYRO_ROLL = "Gyro Roll";
    public static final String GYRO_YAW = "Gyro Yaw";
    public static final String GYRO_ANGLE = "Gyro Angle";
    
    GyroSim m_gyroSim = new GyroSim();

    private GyroSubsystem() {
        init();
    }

    public static GyroSubsystem getInstance() {
        return self == null ? self = new GyroSubsystem() : self;
    }

    public void init() {
        if (ahrsGyro != null) 
            ahrsGyro.reset();
        m_gyroSim.reset();
    }

    public GyroSim getGyroSim() {
        return m_gyroSim;
    }

    public double getRate() {
        if (RuntimeConfig.is_simulator_mode) return m_gyroSim.getRate();

        return ahrsGyro == null ? 0 : ahrsGyro.getRate();
    }

    public double getAngle() {
        if (RuntimeConfig.is_simulator_mode) return m_gyroSim.getAngle();

        return ahrsGyro == null ? 0 : ahrsGyro.getAngle();
    }

    public double getYaw() {
        if (RuntimeConfig.is_simulator_mode) return m_gyroSim.getYaw();

        return ahrsGyro == null ? 0 : ahrsGyro.getYaw();
    }

    public double getRoll() {
        if (RuntimeConfig.is_simulator_mode) return m_gyroSim.getRoll();

        return ahrsGyro == null ? 0 : ahrsGyro.getRoll();
    }

    public double getPitch() {
        if (RuntimeConfig.is_simulator_mode) return m_gyroSim.getPitch();

        return ahrsGyro == null ? 0 : ahrsGyro.getPitch();// - acc.getTilt();
    }

    public Rotation2d getRotation2d() {
        return ahrsGyro == null ? new Rotation2d() : ahrsGyro.getRotation2d();
    }

    public void reset() {
        if (ahrsGyro != null)
            ahrsGyro.reset();
        m_gyroSim.reset();
        
    }

    public void periodic() {
        // System.out.println("gyroP:"+gyro.getPitch()+", gyroR:"+gyro.getDegrees());
        // if ((tickCount & 0x1111) == 0x1111)
        {
            SmartDashboard.putNumber(GYRO_PITCH, getPitch());
            SmartDashboard.putNumber(GYRO_YAW, getYaw());
        }
        // SmartDashboard.putNumber(DriveController.GYRO_ANGLE, gyro.getAngle());
        // SmartDashboard.putNumber(DriveController.GYRO_ROLL, gyro.getRoll());
    }
}