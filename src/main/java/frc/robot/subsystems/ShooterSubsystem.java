package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
    // private final CANSparkMax leftShooter1;
    // private final CANSparkMax leftShooter2;
    // private final CANSparkMax rightShooter1;
    // private final CANSparkMax rightShooter2;

    private final CANSparkMax leftShooter;
    private final CANSparkMax rightShooter;
    private static ShooterSubsystem self;

    public ShooterSubsystem() {
        // leftShooter1 = createMotorController(Constants.ShooterConstants.frontLeftShooterCanId);
        // leftShooter2 = createMotorController(Constants.ShooterConstants.backLeftShooterCanId);
        // rightShooter1 = createMotorController(Constants.ShooterConstants.frontRightShooterCanId);
        // rightShooter2 = createMotorController(Constants.ShooterConstants.backRightShooterCanId);
    
        // leftShooter2.follow(leftShooter1);
        // rightShooter2.follow(rightShooter1);
        leftShooter = createMotorController(Constants.ShooterConstants.leftShooterCanId);
        rightShooter = createMotorController(Constants.ShooterConstants.rightShooterCanId);
    }

    public CANSparkMax createMotorController(int canId) {
        CANSparkMax motor = new CANSparkMax(canId, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kCoast);
        motor.setSmartCurrentLimit(Constants.ShooterConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

        motor.burnFlash();
        return motor;
    }
    
    public static ShooterSubsystem getInstance() {
        return self==null? self=new ShooterSubsystem():self;
    } 
    
    public void startShooterWheels(double targetVelocity) {
        // System.out.println("Shooter in progress");
        leftShooter.set(-targetVelocity); // makes the shooter1 motor rotate at given speed
        rightShooter.set(targetVelocity*.99); // makes the shooter1 motor rotate at given speed
    }

    public void stopShooterWheels() {
        // System.out.println("Stopping...");
        leftShooter.set(0); 
        rightShooter.set(0); 
    }

    public void periodic(){
        SmartDashboard.putNumber("shooter velo", getShooterVelocity());
        super.periodic();
    }

    public double getShooterVelocity() {
        return leftShooter.get();
    }
}