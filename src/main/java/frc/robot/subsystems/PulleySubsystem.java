package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkBase.IdleMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

public class PulleySubsystem extends PositionalableSubsystem {
    private CANSparkMax pulleyMotorRt;
    private CANSparkMax pulleyMotorLt;
    private boolean stopped = true;
    private RelativeEncoder rencoder;
    private double currSpeed = 0;
    private double stoppedPos;
    private double pulleyRange = 0; // Difference between high and low encode values
    private double lowLimit = 0;
    private DifferentialDrive pulley;
    static private PulleySubsystem self;

    private PulleySubsystem() {
        pulleyMotorRt = new CANSparkMax(Constants.PulleyConstants.Pulley_RtCanId, MotorType.kBrushless);
        pulleyMotorLt= new CANSparkMax(Constants.PulleyConstants.Pulley_LtCanId, MotorType.kBrushless);
        rencoder = pulleyMotorRt.getEncoder();
        pulleyMotorRt.setIdleMode(IdleMode.kBrake);
        pulleyMotorLt.setIdleMode(IdleMode.kBrake);
        pulleyMotorRt.setInverted(false);
        pulleyMotorLt.setInverted(true);

        pulley = new DifferentialDrive(pulleyMotorLt, pulleyMotorRt);

        rencoder = pulleyMotorRt.getEncoder();       
/*             
        m_pidController.setP(0.6);
        m_pidController.setI(0.0);
        m_pidController.setD(0.0);
*/
        stoppedPos = rencoder.getPosition();
        SmartDashboard.putNumber(Constants.PulleyConstants.PULLEY_RANGE_LABEL, pulleyRange);

        super.init(pulleyMotorRt);
    }

    public static PulleySubsystem getInstance() {
        if (self==null && Constants.PulleyConstants.Pulley_LtCanId>=0) 
            self =new PulleySubsystem();
        return self;
      }

    public void init() {
        lowLimit = SmartDashboard.getNumber(Constants.PulleyConstants.PULLEY_LOW_LIMIT, lowLimit);
        pulleyRange = SmartDashboard.getNumber(Constants.PulleyConstants.PULLEY_RANGE_LABEL, pulleyRange);
    }

    public void setPosition(double position) {
        rencoder.setPosition(position);
    }

    public double getPosition() {
        return rencoder.getPosition();
    }

    // Returns true if target reached
   
    public Command raiseArmCommand(DoubleSupplier speed) {
        return run(() -> {
            pulley.arcadeDrive(speed.getAsDouble(), 0);
            System.out.println("Arm is pulleying.... with speed: " + speed.getAsDouble());
        }); 
    }

    public Command lowerArmCommand(DoubleSupplier speed) {
        return run(() -> {
            pulley.arcadeDrive(speed.getAsDouble(), 0);
            System.out.println("Arm is lowering.... with speed: " + speed.getAsDouble());
        });
    }

    public Command arcadeDriveCommand(double speed) {
        return run(() -> {
            pulley.arcadeDrive(speed, 0);
            stopped = true;
        }).withName("pulleyStopped");
    }

    public Command pulleyCommand(DoubleSupplier raise) {
        return run(() -> {
          double speed = raise.getAsDouble();
          if (speed>=0.05 || speed<=-0.05) {
            System.out.println("pulleyCommand:"+speed);
            stopped = false;
          } else  {
            speed = 0;
            stopped = true;
          }
          pulley.arcadeDrive(speed, 0);
        }).withName("pulleyDrive");
      }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
        pulley.arcadeDrive(0, 0);
        super.stop();
    }
}