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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RuntimeConfig;
import frc.robot.utils.PID.AsymmetricProfiledPIDController;
import frc.robot.utils.PID.PIDValues;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.Constraints;
import frc.robot.utils.PID.AsymmetricTrapezoidProfile.State;

public abstract class PositionableSubsystem extends SubsystemBase {
    private double currentSpeed = 0;
    private double maxSpeed = 1.0;
    private long arEncoderDifference = 0;
    private AbsoluteEncoder aEncoder;
    private PIDController pid;
    private RelativeEncoder rEncoder;
    private final String name = getName().replace("Subsystem", "");
    private final String ABS_KEY = name + "_ABS";
    private final String REL_KEY = name + "_REL";
    private final String SPEED_KEY = name + " SPEED";
    private final String PIDKP_KEY = name+"_KP";
    private final String PIDKI_KEY = name+"_KI";
    private final String PIDKD_KEY = name+"_KD";
    private final String RANGE_KEY = name+"_RANGE";
    private final String MINEN_KEY = name+"_MINEN";

    /*private Double currentKP = 0.0005;
    private Double currentKI = 0.00;
    private Double currentKD = 0.000;*/

    private Double currentKP = PIDValues.getPID(name).kP;
    private Double currentKI = PIDValues.getPID(name).kI;
    private Double currentKD = PIDValues.getPID(name).kD;

    private long minEncoder = 0;
    private long maxEncoder = 0;
    private long range = 0;
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
        //if (currentSpeed!=0)
            logInfo();
        //updatePIDValues();
    }

    private void updateRange() {
        long r = (long) SmartDashboard.getNumber(RANGE_KEY, range);
        if (r!=range) range = r;
        long m = (long) SmartDashboard.getNumber(MINEN_KEY, minEncoder);
        if (m!=minEncoder) setMinPoint(m);
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

    public void updatePIDValues() {
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
        ShuffleboardTab pidTab = Shuffleboard.getTab("PID");
        pidTab.add(PIDKP_KEY, currentKP);
        pidTab.add(PIDKI_KEY, currentKI);
        pidTab.add(PIDKD_KEY, currentKD);

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

    public void moveToPosition(long pos) {
        moveToPosition(pos,  maxSpeed);
    }

    // Input is absolute position to move to
    public void moveToPosition(long pos, double maxspeed) {
        long currentPos = getPosition(); // relativeToAbsolutePostition(rEncoder.getPosition());
        double speed;
        double asymSpeed;

        //if (currentDestPos==null || currentDestPos!=pos) 
        {
            currentDestPos = pos;
            speed = pid.calculate(currentPos, pos);
            asymSpeed = asmpid.calculate(currentPos, new State(pos, currentSpeed));
        } /*else {
            speed = pid.calculate(currentPos);
            asymSpeed = currentSpeed+asmpid.calculate(currentPos);
        }*/

        // if (pos!=currentPos) //(speed!=0)
        System.out.println("Moving " + name +  " from:" + currentPos +" to:" + pos + ". Calculated PID speed:" + speed+"..asym:"+asymSpeed);
        if (maxSpeed != 0)
            speed = MathUtil.clamp(speed, -maxspeed, maxSpeed);

        move(speed);
        showPositionOnDashboard();
    }

    protected double getCurrentSpeed() {
        return currentSpeed;
    }

    void setMinPoint(long minEncoder) {
        this.minEncoder = minEncoder;
        if (range > 0)
            maxEncoder = minEncoder + range;
    }

    void setRange(long range) {
        this.range = range;
        maxEncoder = minEncoder + range;
        SmartDashboard.putNumber(RANGE_KEY, range);
        SmartDashboard.putNumber(MINEN_KEY, minEncoder);
    }

    protected void setCurrentSpeed(double speed) {
        long currentPosition = getPosition();
        updateRange();
        long delta;
        if (range > 0) {
            if ((delta = (currentPosition - maxEncoder)) >= -10 && (speed * encoderReversed > 0)) {
                System.out.println("Limiting + speed with delta:" + delta);
                speed = delta >= 0 ? 0 : speed * (-delta / 10.0);
            } else if ((delta = (currentPosition - minEncoder)) <= 10 && (speed * encoderReversed < 0)) {
                System.out.println("Limiting - speed with delta:" + delta);
                speed = currentPosition - minEncoder <= 0 ? 0 : speed * (delta / 10.0);
            }
        }
        if (speed != 0) {
            double speedChange =  speed - currentSpeed;
            double timeToPIDSpeedOnAcc = 2; // in seconds
            double timeToPIDSpeedOnDec = 0.1; // in seconds
            boolean accelerating = ((currentSpeed>=0 && speedChange>0) ||(currentSpeed<=0 && speedChange<0));
            double periodsToPIDspeed = accelerating ? timeToPIDSpeedOnAcc*1000/20 : timeToPIDSpeedOnDec*1000/20;
            
            if (Math.abs(speedChange)>0.02)
                speedChange = (speedChange>0?0.02:-0.02)+speedChange / periodsToPIDspeed;
            speed = currentSpeed + speedChange;
            
            System.out.println("Limiting max speed change for " + name + ". Acc:"+accelerating+", change:"+speedChange+"..New speed:" + speed);
            
            if (maxSpeed != 0)
                speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
        }
        currentSpeed = speed;
    }

    public boolean isAtPosition(long pos, Long tolerance) {
        long delta = getPosition() - relativeToAbsolutePostition(pos);
        System.out.println("isAtPosition delta: " + delta);
        return Math.abs(delta) <= tolerance;
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
        //System.out.println("Setting " + name + " position to " + pos);
        if (rEncoder != null)
            rEncoder.setPosition(pos / encoderFactor);
    }

    public void simulationPeriodic() {
        double gravity = -1;
        double curSpeed = getCurrentSpeed();
        if (curSpeed != 0)
            setPosition(getPosition() + encoderReversed * (curSpeed>0?Math.ceil(curSpeed * 100) : Math.floor(curSpeed*100)));
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