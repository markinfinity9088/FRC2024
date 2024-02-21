package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import org.opencv.core.Mat;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RuntimeConfig;

public abstract class PositionableSubsystem extends SubsystemBase {
    private double currentSpeed;
    private double arEncoderDifference;
    private AbsoluteEncoder aEncoder;
    private PIDController pid = new PIDController(0.1, 0, 0);
    private SparkPIDController m_pidController;
    private RelativeEncoder rEncoder;
    private final String name = getName().replace("Subsystem","");
    private final String ABS_KEY = name + "_ABS";
    private final String REL_KEY = name + "_REL";

    abstract void move(double speed);
    public abstract void stop();

    protected void init(CANSparkMax motorController) {
        m_pidController = motorController.getPIDController();
        rEncoder = motorController.getEncoder();
        aEncoder = motorController.getAbsoluteEncoder(Type.kDutyCycle);

        arEncoderDifference = 0; //aEncoder.getPosition()-rEncoder.getPosition();
        
        System.out.println("Initialized "+name+" with arDiff:"+arEncoderDifference);
        m_pidController.setFeedbackDevice(aEncoder);
        // set PID coefficients
        m_pidController.setP(0.1);
        m_pidController.setI(1e-4);
        m_pidController.setD(1);
        m_pidController.setIZone(0);
        m_pidController.setFF(0);
        m_pidController.setOutputRange(1, -1);
        m_pidController.setSmartMotionMaxVelocity(0.5, 0);

        SmartDashboard.putNumber(ABS_KEY, aEncoder.getPosition());
        SmartDashboard.putNumber(REL_KEY, rEncoder.getPosition());
        REVPhysicsSim.getInstance().addSparkMax(motorController, 2.6F, 5676.0F); // DCMotor.getNEO(1));
    }

    double relativeToAbsolutePostition(double poistion) {
        return poistion + arEncoderDifference; // Convert to absolute
    }

    // Input is absolute position to move to
    public void moveToPosition(double pos) {
        double currentPos = RuntimeConfig.is_simulator_mode?rEncoder.getPosition():aEncoder.getPosition(); //relativeToAbsolutePostition(rEncoder.getPosition());
        double speed;
        double MAX_SPEED = 0.5;

        speed =  MathUtil.clamp(pid.calculate(currentPos, pos)*20, -MAX_SPEED, MAX_SPEED);

        System.out.println("Moving "+ name+ " to " + pos + " from CurrentPos:" + currentPos + " with speed:" + speed);

        //m_pidController.setReference(pos, ControlType.kPosition);
        move(speed); // makes the intake motor rotate at given speed

        SmartDashboard.putNumber(ABS_KEY, aEncoder.getPosition());
        SmartDashboard.putNumber(REL_KEY, rEncoder.getPosition());
    }

    protected double getCurrentSpeed() {
        return currentSpeed;
    }

    protected void setCurrentSpeed(double speed) {
        currentSpeed = speed;
        if (speed!=0)
            System.out.println("Moving "+name+" at speed:"+currentSpeed);
    }

    public boolean isAtPosition(double pos) {
        double delta = getPosition() - relativeToAbsolutePostition(pos);
        return Math.abs(delta) < 0.1;
    }

    public double getPosition() {
        return rEncoder == null ? 0 : rEncoder.getPosition();
    }

    public void setPosition(double pos) {
        System.out.println("Setting "+name+" encoder position to " + pos);
        if (rEncoder != null)
            rEncoder.setPosition(pos);
    }

    public void simulationPeriodic() {
        if (getCurrentSpeed() != 0)
            setPosition(getPosition() + getCurrentSpeed() / 10);
    }

    public Command moveCommand(DoubleSupplier speedSupplier) {
        return run(() -> {
            move(speedSupplier.getAsDouble());
            SmartDashboard.putNumber(ABS_KEY, aEncoder.getPosition());
            SmartDashboard.putNumber(REL_KEY, rEncoder.getPosition());
        });
    }

    protected double limitValue(double value, double limit) {
        if (value > limit)
            return limit;
        else if (value < -limit)
            return -limit;
        return value;
    }
}