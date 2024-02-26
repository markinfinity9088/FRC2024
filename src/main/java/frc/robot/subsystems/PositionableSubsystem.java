package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RuntimeConfig;
import frc.robot.utils.PID.AsymmetricProfiledPIDController;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.Constraints;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.State;

public abstract class PositionableSubsystem extends SubsystemBase {
    private double currentSpeed;
    private double maxSpeed = 0;
    private long arEncoderDifference = 0;
    private AbsoluteEncoder aEncoder;
    private PIDController pid;
    private RelativeEncoder rEncoder;
    private final String name = getName().replace("Subsystem", "");
    private final String ABS_KEY = name + "_ABS";
    private final String REL_KEY = name + "_REL";
    private final String SPEED_KEY = name + " SPEED";
    private final String PIDKP_KEY = name + "_KP";
    private final String PIDKI_KEY = name + "_KI";
    private final String PIDKD_KEY = name + "_KD";
    private Double currentKP = 0.01;
    private Double currentKI = 0.0001;
    private Double currentKD = 0.0;
    private long minEncoder = 0;
    private long maxEncoder = 0;
    private Long range = null;
    private int encoderReversed = 1; // 1 if +speed increases encoder, -1 if +speed decreases encoder
    private int encoderFactor = 1000;
    private boolean hasAbsEncoder = false;
    private static int dcount = 0;
    private DoubleLogEntry plogger, slogger;
    private Long currentDestPos = null;

    AsymmetricProfiledPIDController asmpid;

    abstract void move(double speed);

    public abstract void stop();

    public void periodic() {
        logInfo();
        //updatePIDValues();
    }

    public void reset() {
        updatePIDValues();
        rEncoder.setPosition(0);
    }

    protected void setPIDValues(double kP, double kI, double kD) {
        System.out.print("Updating PID controller");
        currentKP = kP;
        currentKI = kI;
        currentKD = kD;
        if (pid==null) {
            pid = new PIDController(kP, kI, kD);
            
            Constraints c = new Constraints(1.0, 1, -1);
            asmpid =  new AsymmetricProfiledPIDController(kP, kI, kD, c);
        } else {
            pid.setPID(kP, kI, kD);
            asmpid.setPID(kP, kI, kD);
        }
        System.out.println("PID values set to:"+pid.getP() + "  " + pid.getI() + "  " + pid.getD());
    }

    private void updatePIDValues() {
        double kPVal = SmartDashboard.getNumber(PIDKP_KEY, 0);
        double kIVal = SmartDashboard.getNumber(PIDKI_KEY, 0);
        double kDVal = SmartDashboard.getNumber(PIDKD_KEY, 0);
        if (kPVal != currentKP || kIVal != currentKI || kDVal != currentKD) {
            setPIDValues(kPVal, kIVal, kDVal);
        }
    }

    public void logInfo() {
        if (plogger!=null)
            plogger.append(getPosition());
        if (slogger!=null)
            slogger.append(getCurrentSpeed());
    }

    public void showPositionOnDashboard() {
        // if (dcount++%16==0)
        {
            // System.out.println(ABS_KEY+":"+aEncoder.getPosition() * encoderFactor);
            if (hasAbsEncoder)
                SmartDashboard.putNumber(ABS_KEY, getAbsPostion());
            SmartDashboard.putNumber(REL_KEY, getRelPostion());
            SmartDashboard.putNumber(SPEED_KEY, currentSpeed);
            logInfo();
            //System.out.print(REL_KEY + ":" + Math.round(rEncoder.getPosition() * encoderFactor));
        }
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setEncoderReversed() {
        encoderReversed = -1;
    }

    protected void init(CANSparkMax motorController) {
        SmartDashboard.putNumber(PIDKP_KEY, currentKP);
        SmartDashboard.putNumber(PIDKI_KEY, currentKI);
        SmartDashboard.putNumber(PIDKD_KEY, currentKD);

        rEncoder = motorController.getEncoder();
        aEncoder = motorController.getAbsoluteEncoder(Type.kDutyCycle);
        if (hasAbsEncoder)
            arEncoderDifference = Math.round((aEncoder.getPosition() - rEncoder.getPosition()) * encoderFactor);
        // SmartDashboard.putNumber(name+"_ARD", arEncoderDifference);
        setPIDValues(currentKP, currentKI, currentKD);
        DataLog log = DataLogManager.getLog();
        plogger = new DoubleLogEntry(log, name+"Pos");
        slogger = new DoubleLogEntry(log, name+"Spd");

        System.out.println("Initialized " + name + " with arDiff:" + arEncoderDifference);

        showPositionOnDashboard();
        if (RuntimeConfig.is_simulator_mode)
            REVPhysicsSim.getInstance().addSparkMax(motorController, 2.6F, 5676.0F); // DCMotor.getNEO(1));
    }

    private long relativeToAbsolutePostition(long position) {
        return position;// + arEncoderDifference; // Convert to absolute
    }

    // Input is absolute position to move to
    public void moveToPosition(long pos) {
        long currentPos = getPosition(); // relativeToAbsolutePostition(rEncoder.getPosition());
        double speed;
        double asymSpeed;
        if (currentDestPos==null || currentDestPos!=pos) {
            currentDestPos = pos;
            speed = pid.calculate(currentPos, pos);
            asymSpeed = asmpid.calculate(currentPos, new State(pos, currentSpeed));
        } else {
            speed = pid.calculate(currentPos);
            asymSpeed = currentSpeed+asmpid.calculate(currentPos);
        }

        // if (pos!=currentPos) //(speed!=0)
        System.out.println("Moving " + name +  " from:" + currentPos +" to:" + pos + ". Calculated PID speed:" + speed+"..asym:"+asymSpeed);

        move(speed);
        showPositionOnDashboard();
    }

    protected double getCurrentSpeed() {
        return currentSpeed;
    }

    void setMinPoint(long minEncoder) {
        this.minEncoder = minEncoder;
        if (range != null)
            maxEncoder = minEncoder + range;
    }

    void setRange(long range) {
        this.range = range;
        maxEncoder = minEncoder + range;
    }

    protected void setCurrentSpeed(double speed) {
        long currentPosition = getPosition();
        long delta;
        if (range != null) {
            if ((delta = (currentPosition - maxEncoder)) >= -10 && (speed * encoderReversed > 0)) {
                System.out.println("Limiting + speed with delta:" + delta);
                speed = delta >= 0 ? 0 : speed * (-delta / 10.0);
            } else if ((delta = (currentPosition - minEncoder)) <= 10 && (speed * encoderReversed < 0)) {
                System.out.println("Limiting - speed with delta:" + delta);
                speed = currentPosition - minEncoder <= 0 ? 0 : speed * (delta / 10.0);
            }
        }
        if (speed != 0) {
            double maxSpeedChange = 0.01;
            if (maxSpeed != 0)
                speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
            if (speed!=0 && Math.abs(currentSpeed - speed) > maxSpeedChange) {
                if (currentSpeed > speed)
                    speed = currentSpeed - maxSpeedChange;
                else
                    speed = currentSpeed + maxSpeedChange;
            }
            System.out.println("Limiting max speed change for " + name + ". New speed:" + speed);
        }
        currentSpeed = speed;
    }

    public boolean isAtPosition(long pos) {
        long delta = getPosition() - relativeToAbsolutePostition(pos);
        System.out.println("isAtPosition delta: " + delta);
        return Math.abs(delta) <= 5;
    }

    private double getAbsPostion() {
        return Math.round((RuntimeConfig.is_simulator_mode ? rEncoder.getPosition() : aEncoder.getPosition()) *encoderFactor);
    }

    private double getRelPostion() {
        return Math.round(rEncoder.getPosition()*encoderFactor);
    }

    public long getPosition() {
        double position = ((!RuntimeConfig.is_simulator_mode) && hasAbsEncoder) ? aEncoder.getPosition()
                : rEncoder.getPosition();
        return Math.round(position * encoderFactor);
    }

    public void setPosition(double pos) {
        System.out.println("Setting " + name + " position to " + pos);
        if (rEncoder != null)
            rEncoder.setPosition(pos / encoderFactor);
    }

    public void simulationPeriodic() {
        double gravity = -1;
        if (getCurrentSpeed() != 0)
            setPosition(getPosition() + encoderReversed * getCurrentSpeed() * 10);
        else if (getPosition() * encoderReversed > 0)
            setPosition(getPosition() + encoderReversed * gravity);
        showPositionOnDashboard();
    }

    public Command moveCommand(DoubleSupplier speedSupplier) {
        return run(() -> {
            move(speedSupplier.getAsDouble());
            showPositionOnDashboard();
        });
    }

    public Command moveUp() {
        return run(() -> {
            move(-.1);
            showPositionOnDashboard();
        });
    }

    public Command moveDown() {
        return run(() -> {
            move(.1);
            showPositionOnDashboard();
        });
    }

    protected double limitValue(double value, double limit) {
        if (value > limit)
            return limit;
        else if (value < -limit)
            return -limit;
        return value;
    }

    public void hasAbsEncoder(boolean b) {
        hasAbsEncoder = b;
    }
}