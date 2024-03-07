package frc.robot.subsystems.simulation;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import frc.robot.subsystems.SparkMaxWrapper;
import edu.wpi.first.wpilibj.simulation.CallbackStore;

public class SwerveModuleSim {
    private final SparkMaxWrapper m_driveMotor;
    private final SparkMaxWrapper m_turnMotor;

    private final EncoderSim m_driveEncoderSim;
    private final EncoderSim m_turnEncoderSim;

    private final Encoder m_driveEncoder;
    private final Encoder m_turnEncoder;

    private final double m_driveKS; //static gain, pass values tuned to your robot example 0.001
    private final double m_driveKV; //velocity feedforward. tune and pass values for your robot, example 0.15
    private final double m_turnKS; //Static gain for turn. example 0.001
    private final double m_turnKV; //Velocity feedforward for turn. Example 0.05

    private final NetworkTableInstance m_netInst = NetworkTableInstance.getDefault();
    private final NetworkTable m_table ;

    //Distance publisher, meters
    private final DoublePublisher m_driveEncoderPubM;
    //Turn distance, radiants
    private final DoublePublisher m_turnEncoderPubRad;
    //drive rate publisher; m/s. turn rate is ignored
    private final DoublePublisher m_driveEncoderRatePubM;
    //motor output publisher , [-1,-1]
    private final DoublePublisher m_drivePWMPub1_1;
    private final DoublePublisher m_turnPWMPub1_1;
    //desired velocity from "inverse feed forward", m/s
    private final DoublePublisher m_driveVPubM_s; 
    //desired velocity from "inverse feed forward", rad/s
    private final DoublePublisher m_turnVPubRad_s;
    //desired velocity from input.
    private final DoublePublisher m_driveVInPubM_s;
    //desired position from input
    private final DoublePublisher m_turnPInPubRad;

    //
    List<CallbackStore> m_cbs = new ArrayList<CallbackStore>();
    private double m_prevTimeSeconds = Timer.getFPGATimestamp();
    private final double m_nominalDts = 0.02; //seconds

    //alpha = 1.5 => between "pink" and random-walk "brownian"
    private final PinkNoise pinkNoise = new PinkNoise(1.5, 3);

    public SwerveModuleSim(SparkMaxWrapper driveMotor, SparkMaxWrapper turnMotor, Encoder driveEncoder, 
                Encoder turnEncoder, double drive_ks, double drive_kv, double turn_ks, double turn_kv, String networkTableName)
    {
        m_table = m_netInst.getTable(networkTableName);

        //create publishers for network table
        m_driveEncoderPubM = m_table.getDoubleTopic("driveEncoderDistanceM").publish();
        m_turnEncoderPubRad = m_table.getDoubleTopic("turnEncoderDistanceRad").publish();
        m_driveEncoderRatePubM = m_table.getDoubleTopic("driveEncoderRateM_s").publish();
        m_drivePWMPub1_1 = m_table.getDoubleTopic("drivePWMOutPut1_1").publish();
        m_turnPWMPub1_1 = m_table.getDoubleTopic("turnPWMOutput1_1").publish();
        m_driveVPubM_s = m_table.getDoubleTopic("driveDesiredSpeedM_s").publish();
        m_turnVPubRad_s = m_table.getDoubleTopic("turnDesiredSpeedRad_s").publish();
        m_driveVInPubM_s = m_table.getDoubleTopic("driveInputSpeedM_s").publish();
        m_turnPInPubRad = m_table.getDoubleTopic("turnInputRad").publish();

       
        m_driveMotor = driveMotor;
        m_turnMotor = turnMotor;

        m_driveEncoder = driveEncoder;
        m_turnEncoder = turnEncoder;

        m_driveEncoderSim = new EncoderSim(driveEncoder);
        m_turnEncoderSim = new EncoderSim(turnEncoder);

        //publish sim values to publishers of network tables
        //pubSim(m_driveMotor, m_drivePWMPub1_1);
        //pubSim(m_turnMotor, m_turnPWMPub1_1);


        m_driveKS = drive_ks;
        m_driveKV = drive_kv;
        m_turnKS = turn_ks;
        m_turnKV = turn_kv;
    }

    /*
    public void pubSim(SparkMaxWrapper sim, DoublePublisher pub) {
        m_cbs.add(sim.registerSpeedCallback((name, value)->pub.set(value.getDouble()), true));
    }
    */

    /*
     * Simulate module drive/steer velocity using heuristics
     */
    public double simulateVelocity(double output, double ks, double kv) {
        //Invert feedforward
        double result = (output - ks * Math.signum(output)) / kv;
        //Add low-frequency note
        result += 0.1 * pinkNoise.nextValue();
        //Add inertia
        return 0.5 * m_driveEncoder.getRate() + 0.5 * result;
    }

    public void simulationInit() {

    }

    public void simulationPeriodic() {
        double currentTimeSeconds = Timer.getFPGATimestamp();
        double dtS = m_prevTimeSeconds >= 0 ? currentTimeSeconds - m_prevTimeSeconds : m_nominalDts;
        m_prevTimeSeconds = currentTimeSeconds;
        simulationPeriodic(dtS);
    }

    public void simulationPeriodic(double dtS) {
        //derive velocity from motor output
        double driveVM_s = simulateVelocity(m_driveMotor.get(), m_driveKS, m_driveKV);
        double turnVRad_s = simulateVelocity(m_turnMotor.get(), m_turnKS, m_turnKV);

        //publish derived velocities
        m_driveVPubM_s.set(driveVM_s);
        m_turnVPubRad_s.set(turnVRad_s);

        //set encoders using desired velocity
        m_driveEncoderSim.setRate(driveVM_s);
        m_driveEncoderSim.setDistance(m_driveEncoderSim.getDistance() + driveVM_s * dtS);
        m_turnEncoderSim.setDistance(m_turnEncoderSim.getDistance() + turnVRad_s * dtS);

        //publish encoder values
        m_driveEncoderPubM.set(m_driveEncoderSim.getDistance());
        m_turnEncoderPubRad.set(m_turnEncoderSim.getDistance());
        m_driveEncoderRatePubM.set(m_driveEncoderSim.getRate());
    }

    public void publishState(SwerveModuleState state) {
        m_driveVInPubM_s.set(state.speedMetersPerSecond);
        m_turnPInPubRad.set(state.angle.getRadians());
    }

    
}
