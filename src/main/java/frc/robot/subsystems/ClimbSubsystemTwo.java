package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;

import frc.robot.Constants;
import frc.robot.Constants.ClimbConstants;

/**
 * This sample program to control 2 motors
 */
public class ClimbSubsystemTwo extends PositionableSubsystem {
    private static final int leadDeviceID = Constants.ClimbConstants.rightClimbCanId;
    private static final int followDeviceID = Constants.ClimbConstants.leftClimbCanId;
    static private ClimbSubsystemTwo self;
    // private double extenisonLimit = 2; //2 feet

    private CANSparkMax m_leadMotor;
    private CANSparkMax m_followMotor;

    // Mark constructor as private so that a new obhject can be creat4ed only using getInstance() method
    private ClimbSubsystemTwo() {
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

        m_leadMotor.setSmartCurrentLimit(ClimbConstants.climbCurrentLimit);
        m_followMotor.setSmartCurrentLimit(ClimbConstants.climbCurrentLimit);

        m_leadMotor.setIdleMode(IdleMode.kBrake);
        m_followMotor.setIdleMode(IdleMode.kBrake);  

        /**
         * In CAN mode, one SPARK MAX can be configured to follow another. This is done
         * by calling the follow() method on the SPARK MAX you want to configure as a follower, and
         * by passing as a parameter the SPARK MAX you want to configure as a leader.
         */
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 100);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);
        m_followMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus7, 1000);
        //  m_drive = new DifferentialDrive(m_leadMotor, m_followMotor);


        super.init(m_leadMotor);
        super.setMaxSpeed(Constants.ClimbConstants.MAX_SPEED);
        m_leadMotor.burnFlash();
        m_followMotor.burnFlash();
    }

    // Method to create new singleton object
    public static ClimbSubsystemTwo getInstance() {
        if (self==null && Constants.ClimbConstants.leftClimbCanId>=0) 
            self =new ClimbSubsystemTwo();
        return self;
      }

    public void moveRight(double speed) {
        // System.out.println("Climb speed:"+speed);
        m_leadMotor.set(speed);

        // setCurrentSpeed(speed);
        // m_drive.arcadeDrive(getCurrentSpeed(), 0);
    }

    public void moveLeft(double speed) {
        // System.out.println("Climb speed:"+speed);
        m_followMotor.set(speed);

        // setCurrentSpeed(speed);
        // m_drive.arcadeDrive(getCurrentSpeed(), 0);
    }



    public void stop() {
        move(0);
        // System.out.println("Hook stopped");
    }

    @Override
    public void move(double speed) {
        moveLeft(speed);
        moveRight(speed);
    
    }
}