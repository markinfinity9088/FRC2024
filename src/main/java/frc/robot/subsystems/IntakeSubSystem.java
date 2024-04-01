package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.vision.limelight.LimeLightFacade;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class IntakeSubSystem extends SubsystemBase {


  //If this flag is false then we use color to detect ring otherwise proximity
  boolean useSensorForDetection = true;

  //Fine tune this
  static final double kProximityForRingDetection = 175;

  static final double intakeSpeed = 1;
  static final double  releaseToAMPSpeed = -0.8;
  static final double releaseToShooterSpeed = 1.0;
  static IntakeSubSystem self;
  static final int INTAKE_CURRENT_LIMIT_A = 30; // How many amps the intake can use while picking up
  private CANSparkMax intake = null;
  DigitalInput beamBreak;
  LimeLightFacade limelight;
  //tweak the colors later



  private IntakeSubSystem() {
 

    beamBreak = new DigitalInput(Constants.IntakeConstants.beamBreakDIO);
    limelight = LimeLightFacade.getInstance();
    intake = new CANSparkMax(Constants.IntakeConstants.intakeCanId, MotorType.kBrushless);
    intake.setIdleMode(IdleMode.kBrake);
    intake.setSmartCurrentLimit(INTAKE_CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive
    intake.burnFlash();
  }

  public static IntakeSubSystem getInstance() {
    if (self == null && Constants.IntakeConstants.intakeCanId >= 0)
      self = new IntakeSubSystem();
    return self;
  }



  public void setUseSensorForDetection(boolean flag) {
    useSensorForDetection = flag;
  }

  public void doIntake() {
    doIntake(intakeSpeed);
  }

  public void doIntake(double intakeSpeed) {
    // if (intakeSpeed!=0) System.out.println("Intake in progress");
    intake.set(intakeSpeed); // makes the intake motor rotate at given speed
  }

  public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      double speed = speedSupplier.getAsDouble();
      doIntake(speed);
    });
  }

  public void releaseToAMP() {
    // System.out.println("Releasing to AMP..");
    intake.set(releaseToAMPSpeed);
  }

  public void releaseToShooter() {
    // System.out.println("Releasing to Shooter..");
    intake.set(releaseToShooterSpeed);
  }

  public void stop() {
    // System.out.println("Stopping...");
    intake.set(0);
  }



  @Override
  public void periodic() {



    boolean ringDetected = !beamBreak.get();
    SmartDashboard.putBoolean("ringDetected", ringDetected);
    // limelight.setLED(ringDetected);

    //Color color = intakeColorSensor.getColor();

   // String colorstr = ""+color.red+":"+color.green+":"+color.blue;
    // SmartDashboard.putString("IntakeColors", colorstr);

    // SmartDashboard.putBoolean("RingColorMatched", isMatchingColor());
    // SmartDashboard.putBoolean("RingDistanceMatched", isMatchingDistance());
  }

public boolean isRingDetected() {
  boolean ringDetected = !beamBreak.get();
  SmartDashboard.putBoolean("ringDetected", ringDetected);
  // limelight.setLED(ringDetected);
  return ringDetected;

}
}