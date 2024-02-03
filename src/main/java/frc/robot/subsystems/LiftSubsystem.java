package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkBase.IdleMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.LiftConstants;

public class LiftSubsystem extends SubsystemBase{
    private final CANSparkMax liftMotorRt = new CANSparkMax(Constants.LiftConstants.LIFT_RT, MotorType.kBrushless);
    private final CANSparkMax liftMotorLt= new CANSparkMax(Constants.LiftConstants.LIFT_LT, MotorType.kBrushless);
    private boolean stopped = true;
    private RelativeEncoder m_encoder;
    private double currSpeed = 0;
    private double stoppedPos;
    private double liftRange = 0; // Difference between high and low encode values
    private double lowLimit = 0;
    private final DifferentialDrive lift = new DifferentialDrive(liftMotorLt, liftMotorRt);
    static private LiftSubsystem self;

    private LiftSubsystem() {
        liftMotorRt.setIdleMode(IdleMode.kBrake);
        liftMotorLt.setIdleMode(IdleMode.kBrake);
        liftMotorRt.setInverted(false);
        liftMotorLt.setInverted(true);
        m_encoder = liftMotorRt.getEncoder();       
/*             
        m_pidController.setP(0.6);
        m_pidController.setI(0.0);
        m_pidController.setD(0.0);
*/
        stoppedPos = m_encoder.getPosition();
        SmartDashboard.putNumber(LiftConstants.LIFT_RANGE_LABEL, liftRange);
    }

    public static LiftSubsystem getInstance() {
        if (self==null) self =new LiftSubsystem();
        return self;
      }

    public void init() {
        lowLimit = SmartDashboard.getNumber(LiftConstants.LIFT_LOW_LIMIT, lowLimit);
        liftRange = SmartDashboard.getNumber(LiftConstants.LIFT_RANGE_LABEL, liftRange);
    }

    public void setPosition(double position) {
        m_encoder.setPosition(position);
    }

    public double getPosition() {
        return m_encoder.getPosition();
    }

    // Returns true if target reached
   
    public Command raiseArmCommand(DoubleSupplier speed) {
        return run(() -> {
            lift.arcadeDrive(speed.getAsDouble(), 0);
            System.out.println("Arm is lifting.... with speed: " + speed.getAsDouble());
        }); 
    }

    public Command lowerArmCommand(DoubleSupplier speed) {
        return run(() -> {
            lift.arcadeDrive(speed.getAsDouble(), 0);
            System.out.println("Arm is lowering.... with speed: " + speed.getAsDouble());
        });
    }

    public Command arcadeDriveCommand(double speed) {
        return run(() -> {
            lift.arcadeDrive(speed, 0);
            stopped = true;
        }).withName("liftStopped");
    }

    public Command liftCommand(DoubleSupplier raise) {
        return run(() -> {
          double speed = raise.getAsDouble();
          if (speed>=0.05 || speed<=-0.05) {
            System.out.println("liftCommand:"+speed);
            stopped = false;
          } else  {
            speed = 0;
            stopped = true;
          }
          lift.arcadeDrive(speed, 0);
        }).withName("liftDrive");
      }

    public boolean isStopped() {
        return stopped;
    }

    public double getCurrentSpeed() {
        return currSpeed;
    }

    
}