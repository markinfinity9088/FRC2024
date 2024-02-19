package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import frc.robot.Constants;

//

public class ShooterSubsystem extends PositionableSubsystem{ //positionableSybsystem is used for tilt position

    private final CANSparkMax leftShooter1;
    private final CANSparkMax leftShooter2;
    private final CANSparkMax rightShooter1;
    private final CANSparkMax rightShooter2;
    private final CANSparkMax tiltMotor;
    private static ShooterSubsystem self;

    public ShooterSubsystem() {
       

        leftShooter1 = createMotorController(Constants.ShooterConstants.leftShooter1CanId);
        leftShooter2 = createMotorController(Constants.ShooterConstants.leftShooter2CanId);
        rightShooter1 = createMotorController(Constants.ShooterConstants.rightShooter1CanId);
        rightShooter2 = createMotorController(Constants.ShooterConstants.rightShooter2CanId);
        tiltMotor = createMotorController(Constants.ShooterConstants.tiltMotorCanId);
    
        leftShooter2.follow(leftShooter1);
        rightShooter2.follow(rightShooter1);
     
        super.init(tiltMotor);

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
        leftShooter1.set(targetVelocity); // makes the shooter1 motor rotate at given speed
        rightShooter1.set(targetVelocity * -1); // makes the shooter1 motor rotate at given speed
        


    }

    public void stopShooterWheels() {
        System.out.println("Stopping...");
        leftShooter1.set(0); 
        rightShooter1.set(0); 

    }

    public double getShooterVelocity() {
        return leftShooter1.get();
    }

    public void tiltToAngle(double degrees) {
        //to do
        //calculate position of tilt motor for angle
        //call method moveToPosition of rightClimbCanId

         
    }


    public void runTilt(double speed) {
        System.out.println("Tilting in progress");
        tiltMotor.set(speed); 

    }

    public void stopTilt() {
        System.out.println("Stopping...");
        tiltMotor.set(0); 

    }


    //For PositionalSubsystem
    @Override
    void move(double speed) { //Tilt action
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public void stop() { //stop tilt
        // TODO Auto-generated method stub
        stopTilt();
    }


    

}

