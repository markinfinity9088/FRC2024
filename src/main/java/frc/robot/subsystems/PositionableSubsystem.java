package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;

import java.util.function.DoubleSupplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class PositionableSubsystem extends SubsystemBase {
    private double currentSpeed;
    private double arEncoderDifference;
    private AbsoluteEncoder aencoder;
    private SparkPIDController m_pidController;
    private RelativeEncoder posEncoder;
    private final String ABS_KEY = getName().replace("Subsystem","") + "_ABS";
    private final String REL_KEY = getName().replace("Subsystem","") + "_REL";

    abstract void move(double speed);
    public abstract void stop();

    protected void init(CANSparkMax motorController) {
        m_pidController = motorController.getPIDController();
        posEncoder = motorController.getEncoder();
        aencoder = motorController.getAbsoluteEncoder(Type.kDutyCycle);

        arEncoderDifference = posEncoder.getPosition() - aencoder.getPosition();
        m_pidController.setFeedbackDevice(posEncoder);
        // set PID coefficients
        m_pidController.setP(0.1);
        m_pidController.setI(1e-4);
        m_pidController.setD(1);
        m_pidController.setIZone(0);
        m_pidController.setFF(0);
        m_pidController.setOutputRange(1, -1);

        SmartDashboard.putNumber(ABS_KEY, aencoder.getPosition());
        SmartDashboard.putNumber(REL_KEY, posEncoder.getPosition());
        REVPhysicsSim.getInstance().addSparkMax(motorController, 2.6F, 5676.0F); // DCMotor.getNEO(1));
    }

    double relativeToAbsolutePostition(double poistion) {
        return poistion + arEncoderDifference; // Convert to relative
    }

    // Input is absolute position to move to
    public void moveToPosition(double pos) {
        double currentPos = relativeToAbsolutePostition(posEncoder.getPosition());

        currentSpeed = (pos - currentPos) / 10; // Just to help with simulation

        System.out.println("Moving " + getName() + " to " + pos + ".. CurrentPos:" + currentPos + " with currentSpeed:"
                + currentSpeed);

        m_pidController.setReference(pos, ControlType.kPosition);
        SmartDashboard.putNumber(ABS_KEY, aencoder.getPosition());
        SmartDashboard.putNumber(REL_KEY, posEncoder.getPosition());
        // move(speed); // makes the intake motor rotate at given speed*/
    }

    protected double getCurrentSpeed() {
        return currentSpeed;
    }

    protected void setCurrentSpeed(double speed) {
        currentSpeed = speed;
    }

    public boolean isAtPosition(double pos) {
        double delta = getPosition() - relativeToAbsolutePostition(pos);
        return Math.abs(delta) < 0.25;
    }

    public double getPosition() {
        return posEncoder == null ? 0 : posEncoder.getPosition();
    }

    public void setPosition(double pos) {
        System.out.println("Setting encoder position to " + pos);
        if (posEncoder != null)
            posEncoder.setPosition(pos);
    }

    public void simulationPeriodic() {
        if (getCurrentSpeed() != 0)
            setPosition(getPosition() + getCurrentSpeed() / 10);
    }

    public Command moveCommand(DoubleSupplier speedSupplier) {
        return run(() -> {
            move(speedSupplier.getAsDouble());
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