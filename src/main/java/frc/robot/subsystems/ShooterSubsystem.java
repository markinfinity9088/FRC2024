package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
    private final CANSparkMax leftShooter1;
    private final CANSparkMax leftShooter2;
    private final CANSparkMax rightShooter1;
    private final CANSparkMax rightShooter2;
    private static ShooterSubsystem self;

    public ShooterSubsystem() {
        leftShooter1 = createMotorController(Constants.ShooterConstants.frontLeftShooterCanId);
        leftShooter2 = createMotorController(Constants.ShooterConstants.backLeftShooterCanId);
        rightShooter1 = createMotorController(Constants.ShooterConstants.frontRightShooterCanId);
        rightShooter2 = createMotorController(Constants.ShooterConstants.backRightShooterCanId);
    
        leftShooter2.follow(leftShooter1);
        rightShooter2.follow(rightShooter1);
    }

    public CANSparkMax createMotorController(int canId) {
        CANSparkMax motor = new CANSparkMax(canId, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setSmartCurrentLimit(Constants.ShooterConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

        return motor;
    }
    
    public static ShooterSubsystem getInstance() {
        return self==null? self=new ShooterSubsystem():self;
    } 
    
    public void startShooterWheels(double targetVelocity) {
        System.out.println("Shooter in progress");
        leftShooter1.set(-targetVelocity); // makes the shooter1 motor rotate at given speed
        rightShooter1.set(targetVelocity); // makes the shooter1 motor rotate at given speed
    }

    public void stopShooterWheels() {
        System.out.println("Stopping...");
        leftShooter1.set(0); 
        rightShooter1.set(0); 
    }

    public double getShooterVelocity() {
        return leftShooter1.get();
    }
}