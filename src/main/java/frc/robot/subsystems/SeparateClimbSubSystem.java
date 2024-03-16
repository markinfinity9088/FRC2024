package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SeparateClimbSubSystem extends SubsystemBase {
    private static final int leadDeviceID = Constants.ClimbConstants.rightClimbCanId;
    private static final int followDeviceID = Constants.ClimbConstants.leftClimbCanId;
    static private SeparateClimbSubSystem self;
    // private double extenisonLimit = 2; //2 feet
    private CANSparkMax m_leftMotor;
    private CANSparkMax m_rightMotor;

    private double m_maxspeed = Constants.ClimbConstants.MAX_SPEED;

    // Mark constructor as private so that a new obhject can be creat4ed only using getInstance() method
    private SeparateClimbSubSystem() {
        /**
         * SPARK MAX controllers are intialized over CAN by constructing a CANSparkMax
         * object
         *
         */
        m_leftMotor = new CANSparkMax(leadDeviceID, MotorType.kBrushless);
        m_rightMotor = new CANSparkMax(followDeviceID, MotorType.kBrushless);
        m_rightMotor.setInverted(true);
          
        /**
         * The RestoreFactoryDefaults method to reset the configuration
         */
        m_leftMotor.restoreFactoryDefaults();
        m_rightMotor.restoreFactoryDefaults();

        m_leftMotor.setIdleMode(IdleMode.kBrake);
        m_rightMotor.setIdleMode(IdleMode.kBrake);  

        m_leftMotor.burnFlash();
        m_rightMotor.burnFlash();
    }

    public void setMaxSpeed(double maxspeed) {
        m_maxspeed = maxspeed;
    }

    // Method to create new singleton object
    public static SeparateClimbSubSystem getInstance() {
        if (self==null && Constants.ClimbConstants.leftClimbCanId>=0) 
            self =new SeparateClimbSubSystem();
        return self;
      }

    public void move(double speed) {
        moveLeft(speed);
        moveRight(speed);
    }

    //use separate to control separately
    public void moveLeft(double speed)  {
        speed = MathUtil.clamp(speed, -m_maxspeed, m_maxspeed);
        System.out.println("Climb left speed:"+speed);
        m_leftMotor.set(speed);
    }

    public void moveRight(double speed) {
        speed = MathUtil.clamp(speed, -m_maxspeed, m_maxspeed);
        System.out.println("Climb right speed:"+speed);
        m_rightMotor.set(speed);
    }

    public void stop() {
        move(0);
        System.out.println("Hook stopped");
    }
}
