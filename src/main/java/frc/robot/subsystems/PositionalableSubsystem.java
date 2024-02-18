package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class PositionalableSubsystem extends SubsystemBase {
    private double currentSpeed;
    private double arEncoderDifference;

    private SparkPIDController m_pidController;
    private RelativeEncoder posEncoder;

    protected void init(CANSparkMax motorController) {
        m_pidController = motorController.getPIDController();
        posEncoder = motorController.getEncoder();
        AbsoluteEncoder aencoder = motorController.getAbsoluteEncoder(Type.kDutyCycle);

        arEncoderDifference = posEncoder.getPosition() - aencoder.getPosition();
    }

    double relativeToAbsolutePostition(double poistion) {
        return poistion + arEncoderDifference; // Convert to relative
    }

    // Input is absolute position to move to
    public void moveToPosition(double pos) {
        double currentPos = relativeToAbsolutePostition(posEncoder.getPosition());

        currentSpeed = (pos - currentPos)/Math.abs(pos); // Just to help with simulation

        System.out.println("Moving to " + pos + ".. CurrentPos:" + currentPos + " with currentSpeed:"
                + currentSpeed);

        m_pidController.setReference(pos, ControlType.kPosition);
        // move(speed); // makes the intake motor rotate at given speed*/
    }

    protected double getCurrentSpeed() {
        return currentSpeed;
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

    public void stop() {
        currentSpeed = 0;
    }

    public void simulationPeriodic() {
        if (getCurrentSpeed() != 0)
            setPosition(getPosition() + getCurrentSpeed() / 10);
    }
}