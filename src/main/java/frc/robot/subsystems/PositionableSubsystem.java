package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RuntimeConfig;

public abstract class PositionableSubsystem extends SubsystemBase {
    private double currentSpeed;
    private double maxSpeed = 0;
    private long arEncoderDifference = 0;
    private AbsoluteEncoder aEncoder;
    private PIDController pid = new PIDController(.001, 0.0001, 0);
    private RelativeEncoder rEncoder;
    private final String name = getName().replace("Subsystem", "");
    private final String ABS_KEY = name + "_ABS";
    private final String REL_KEY = name + "_REL";
    private final String SPEED_KEY = name + " SPEED";
    private final String PIDKP_KEY=name+"_KP";
    private final String PIDKI_KEY=name+"_KI";
    private final String PIDKD_KEY=name+"_KD";
    private Double currentKP = 0.1;
    private Double currentKI = 0.0001;
    private Double currentKD = 0.0;
    private long minEncoder = 0;
    private long maxEncoder = 0;
    private Long range = null;
    private int encoderReversed = 1; // 1 if +speed increases encoder, -1 if +speed decreases encoder
    private int encoderFactor = 1000;
    private boolean hasAbsEncoder = false;
    private static int dcount = 0;

    abstract void move(double speed);

    public abstract void stop();
    public void setPIDValues(double kP, double kI, double kD){
        pid = new PIDController(kP, kI, kD);
        currentKP = kP;
        currentKI = kI;
        currentKD = kD;
    }

    public void showPositionOnDashboard() {
        //if (dcount++%16==0) 
        {
            //System.out.println(ABS_KEY+":"+aEncoder.getPosition() * encoderFactor);
            //if (hasAbsEncoder)
                SmartDashboard.putNumber(ABS_KEY, Math.round(aEncoder.getPosition() * encoderFactor));
            SmartDashboard.putNumber(REL_KEY, Math.round(rEncoder.getPosition() * encoderFactor));
            SmartDashboard.putNumber(SPEED_KEY, currentSpeed);

            System.out.print(REL_KEY+":"+Math.round(rEncoder.getPosition() * encoderFactor));
            
           
            
        }
    }
    public void updatePIDValues() {
        double kPVal = SmartDashboard.getNumber(PIDKP_KEY, 0);
        double kIVal = SmartDashboard.getNumber(PIDKI_KEY, 0);
        double kDVal = SmartDashboard.getNumber(PIDKD_KEY, 0);



            if (kPVal != currentKP || kIVal != currentKI || kDVal != currentKD){
                setPIDValues(kPVal, kIVal, kDVal);
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
        if (hasAbsEncoder==true)
            arEncoderDifference = Math.round((aEncoder.getPosition() - rEncoder.getPosition()) * encoderFactor);
        //SmartDashboard.putNumber(name+"_ARD", arEncoderDifference);

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

        speed = pid.calculate(currentPos, pos);
        //if (pos!=currentPos) //(speed!=0)
            System.out.println("Moving " + name + " to: " + pos + " from:" + currentPos + ". Calculated speed:" + speed);

        move(speed);
        showPositionOnDashboard();

        System.out.println(pid.getP() + "  " + pid.getI() + "  " + pid.getD()); 
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
        if (range!=null) {
            if ((delta=(currentPosition-maxEncoder)) >= -10 && (speed*encoderReversed > 0)) {
                System.out.println("Limiting + speed with delta:"+delta);
                speed = delta>=0 ? 0 : speed*(-delta/10.0);
            } else if ((delta=(currentPosition-minEncoder)) <= 10  && (speed*encoderReversed < 0)) {
                System.out.println("Limiting - speed with delta:"+delta);
                speed = currentPosition-minEncoder<=0 ? 0 : speed*(delta/10.0);
            }
        }  
        if ((speed != 0) || currentSpeed != 0) {
            if (maxSpeed != 0)
                speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);
            if (Math.abs(currentSpeed-speed)>0.05) {
                if (currentSpeed>speed) currentSpeed -= 0.05; else currentSpeed += 0.05;
            }
            System.out.println("Set " + name + " speed:" + speed);
        }
        currentSpeed = speed;
    }

    public boolean isAtPosition(long pos) {
        long delta = getPosition() - relativeToAbsolutePostition(pos);
        System.out.println("isAtPosition delta: "+delta);
        return Math.abs(delta) <= 10;
    }

    public long getPosition() {
        double position = ((!RuntimeConfig.is_simulator_mode) && hasAbsEncoder) ? aEncoder.getPosition()
                : rEncoder.getPosition();
        return Math.round(position * encoderFactor);
    }

    public void setPosition(double pos) {
        System.out.println("Setting " + name + " position to " + pos);
        if (rEncoder != null)
            rEncoder.setPosition(pos/encoderFactor);
    }

    public void simulationPeriodic() {
        double gravity = -1;
        if (getCurrentSpeed() != 0)
            setPosition(getPosition() + encoderReversed*getCurrentSpeed()*10);
        else if (getPosition()*encoderReversed>=0)
            setPosition(getPosition() + encoderReversed*gravity);
        showPositionOnDashboard();
    }

    public Command moveCommand(DoubleSupplier speedSupplier) {
        return run(() -> {
            move(speedSupplier.getAsDouble());
            showPositionOnDashboard();
        });
    }

    public Command moveUp(){
        return run(() -> {
            move(-.1);
            showPositionOnDashboard();
        });
    }

    public Command moveDown(){
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