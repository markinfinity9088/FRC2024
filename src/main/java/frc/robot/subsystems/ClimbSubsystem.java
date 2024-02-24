package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;

/**
 * This sample program to control 2 motors
 */
public class ClimbSubsystem extends PositionableSubsystem {
    private static final int leadDeviceID = Constants.ClimbConstants.rightClimbCanId;
    private static final int followDeviceID = Constants.ClimbConstants.leftClimbCanId;
    static private ClimbSubsystem self;
    // private double extenisonLimit = 2; //2 feet
    private DifferentialDrive m_drive;
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
        m_followMotor.setInverted(true);


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
        //m_followMotor.follow(m_leadMotor);
         m_drive = new DifferentialDrive(m_leadMotor, m_followMotor);


        super.init(m_leadMotor);
        super.setMaxSpeed(Constants.ClimbConstants.MAX_SPEED);
    }

    // Method to create new singleton object
    public static ClimbSubsystem getInstance() {
        if (self==null && Constants.ClimbConstants.leftClimbCanId>=0) 
            self =new ClimbSubsystem();
        return self;
      }

    public void move(double speed) {
        System.out.println("Climb speed:"+speed);
        setCurrentSpeed(speed);
        m_drive.arcadeDrive(getCurrentSpeed(), 0);
    }

    public void stop() {
        move(0);
        System.out.println("Hook stopped");
    }
}