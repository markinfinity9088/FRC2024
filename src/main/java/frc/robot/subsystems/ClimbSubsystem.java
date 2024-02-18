package frc.robot.subsystems;

import java.util.function.DoubleSupplier;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * This sample program to control 2 motors
 */
public class ClimbSubsystem extends SubsystemBase {
    private static final int leadDeviceID = 16;
    private static final int followDeviceID = 17;
    static private ClimbSubsystem self;
    // private double extenisonLimit = 2; //2 feet

    private CANSparkMax m_leadMotor;
    private CANSparkMax m_followMotor;

    // Mark constructor as private so that a new obhject can be creat4ed only using getInstance() method
    private ClimbSubsystem() {
        /**
         * SPARK MAX controllers are intialized over CAN by constructing a CANSparkMax
         * object
         *
         */
        m_leadMotor = new CANSparkMax(leadDeviceID, MotorType.kBrushless);
        m_followMotor = new CANSparkMax(followDeviceID, MotorType.kBrushless);

        /**
         * The RestoreFactoryDefaults method to reset the configuration
         */
        m_leadMotor.restoreFactoryDefaults();
        m_followMotor.restoreFactoryDefaults();

        /**
         * In CAN mode, one SPARK MAX can be configured to follow another. This is done
         * by calling the follow() method on the SPARK MAX you want to configure as a follower, and
         * by passing as a parameter the SPARK MAX you want to configure as a leader.
         */
        m_followMotor.follow(m_leadMotor);
    }

    // Method to create new singleton object
    public static ClimbSubsystem getInstance() {
        if (self==null && Constants.ClimbConstants.Climb_LtCanId>=0) 
            self =new ClimbSubsystem();
        return self;
      }

    public Command raiseCommand(DoubleSupplier speed) {
        return run(() -> {
            m_leadMotor.set(speed.getAsDouble());
            System.out.println("Hook is raising.... with speed: " + speed.getAsDouble());
        });
    }

    public Command lowerCommand(DoubleSupplier speed) {
        return run(() -> {
            m_leadMotor.set(speed.getAsDouble());
            System.out.println("Hook is lowering.... with speed: " + speed.getAsDouble());
        });
    }
}