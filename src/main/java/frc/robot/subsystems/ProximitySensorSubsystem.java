package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ProximitySensorSubsystem extends SubsystemBase {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final int proximityLimit = 90;
    private static ProximitySensorSubsystem self;
    public static final String PROXIMITY="Proximity";

    private ProximitySensorSubsystem() {
        SmartDashboard.putNumber(PROXIMITY, m_colorSensor.getProximity());
    }

    public static ProximitySensorSubsystem getInstance() {
        return self==null? self=new ProximitySensorSubsystem():self;
    }

    public boolean isNear() {
        SmartDashboard.putNumber(PROXIMITY, m_colorSensor.getProximity());
        return m_colorSensor.getProximity() > proximityLimit;
    }
}