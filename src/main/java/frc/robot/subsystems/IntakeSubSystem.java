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
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class IntakeSubSystem extends SubsystemBase {

  //Change port based on where it is connected to
  static final I2C.Port ColorSensorPort = I2C.Port.kOnboard;
  private ColorSensorV3 intakeColorSensor ;
  
  private ColorMatch m_ColorMatch;

  //tweak this later
  static final Color kRingTarget = new Color(0.143, 0.427, 0.429);
  

  //If this flag is false then we use color to detect ring otherwise proximity
  boolean useDistanceForIntakeDetection = true;

  //Fine tune this
  static final double kProximityForRingDetection = 0.4;

  static final double intakeSpeed = 1.0;
  static final double releaseToAMPSpeed = -0.8;
  static final double releaseToShooterSpeed = 1.0;
  static IntakeSubSystem self;
  static final int INTAKE_CURRENT_LIMIT_A = 30; // How many amps the intake can use while picking up
  private CANSparkMax intake = null;

  //tweak the colors later



  private IntakeSubSystem() {
    try {
      intakeColorSensor = new ColorSensorV3(ColorSensorPort);
    } catch(Exception e) {

    }

    m_ColorMatch = new ColorMatch();
    addColorMatchValues();
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

  private void addColorMatchValues() {
    m_ColorMatch.addColorMatch(kRingTarget);
    
  }

  public void setUseDistanceForIntakeDetection(boolean flag) {
    useDistanceForIntakeDetection = flag;
  }

  public void doIntake() {
    doIntake(intakeSpeed);
  }

  public void doIntake(double intakeSpeed) {
    if (intakeSpeed!=0) System.out.println("Intake in progress");
    intake.set(intakeSpeed); // makes the intake motor rotate at given speed
  }

  public Command moveCommand(DoubleSupplier speedSupplier) {
    return run(() -> {
      double speed = speedSupplier.getAsDouble();
      doIntake(speed);
    });
  }

  public void releaseToAMP() {
    System.out.println("Releasing to AMP..");
    intake.set(releaseToAMPSpeed);
  }

  public void releaseToShooter() {
    System.out.println("Releasing to Shooter..");
    intake.set(releaseToShooterSpeed);
  }

  public void stop() {
    System.out.println("Stopping...");
    intake.set(0);
  }

  //tweak this later
  public boolean isMatchingColor() {
    if (intakeColorSensor == null) {
      return false;
    }

    Color detectedColor = intakeColorSensor.getColor();
    ColorMatchResult match = m_ColorMatch.matchClosestColor(detectedColor);

    if (match.color == kRingTarget) {
      return true;
    }

    return false;
  }

  public boolean isMatchingDistance() {
    if (intakeColorSensor == null) {
      return false;
    }
    /* Get the raw proximity value from the sensor ADC (11 bit). This value is largest when an object
    * is close to the sensor and smallest when far away. */
 
    double proximity = intakeColorSensor.getProximity();
    return (proximity >= kProximityForRingDetection);
  }

  @Override
  public void periodic() {

    if (intakeColorSensor == null) {
      SmartDashboard.putString("ColorSensor", "Not found");
      return;
    }

    Color color = intakeColorSensor.getColor();
  
    SmartDashboard.putNumber("Ring Proximity", intakeColorSensor.getProximity());
    String colorstr = ""+color.red+":"+color.green+":"+color.blue;
    SmartDashboard.putString("IntakeColors", colorstr);

    SmartDashboard.putBoolean("RingColorMatched", isMatchingColor());
    SmartDashboard.putBoolean("RingDistanceMatched", isMatchingDistance());
  }

public boolean isRingDetected() {
    if (useDistanceForIntakeDetection) {
      return isMatchingDistance();
    }
    return isMatchingColor();
}
}