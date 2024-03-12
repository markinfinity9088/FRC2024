package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import org.opencv.core.Mat;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants.AutoConstants;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.GeneralConstants;
import frc.robot.utils.SwerveUtils;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDriveSubsystem extends SubsystemBase  {
  // Create MAXSwerveModules
  private final MAXSwerveModule m_frontLeft = new MAXSwerveModule(
      DriveConstants.kFrontLeftDrivingCanId,
      DriveConstants.kFrontLeftTurningCanId,
      DriveConstants.kFrontLeftChassisAngularOffset);

  private final MAXSwerveModule m_frontRight = new MAXSwerveModule(
      DriveConstants.kFrontRightDrivingCanId,
      DriveConstants.kFrontRightTurningCanId,
      DriveConstants.kFrontRightChassisAngularOffset);

  private final MAXSwerveModule m_rearLeft = new MAXSwerveModule(
      DriveConstants.kRearLeftDrivingCanId,
      DriveConstants.kRearLeftTurningCanId,
      DriveConstants.kBackLeftChassisAngularOffset);

  private final MAXSwerveModule m_rearRight = new MAXSwerveModule(
      DriveConstants.kRearRightDrivingCanId,
      DriveConstants.kRearRightTurningCanId,
      DriveConstants.kBackRightChassisAngularOffset);

  // The gyro sensor
  private final GyroSubsystem m_gyro = GyroSubsystem.getInstance();

  // Slew rate filter variables for controlling lateral acceleration
  private double m_currentRotation = 0.0;
  private double m_currentTranslationDir = 0.0;
  private double m_currentTranslationMag = 0.0;


  double xSpeedDelivered = 0, ySpeedDelivered = 0, rotDelivered=0;

  private SlewRateLimiter m_magLimiter = new SlewRateLimiter(DriveConstants.kMagnitudeSlewRate);
  private SlewRateLimiter m_rotLimiter = new SlewRateLimiter(DriveConstants.kRotationalSlewRate);
  private double m_prevTime = WPIUtilJNI.now() * 1e-6;

  private double maximum_drive_speed = DriveConstants.kMaxDriveSubsystemSpeed; //0-1
  private double maximum_rotation_speed = DriveConstants.kMaxDriveSubsystemTurnSpeed;

  //KP hacky way to correct swerve drift with field relative + swerve hardware issue
  private Rotation2d m_lastRecordGyroBeforeRotation;

  private SwerveDriveSubsystem(){
    System.out.println("Swerve Drive Subsystem Created");
    AutoBuilder.configureHolonomic(
      this::getPose,
      this::resetOdometry,
      this::getRobotRelativeSpeeds,
      this::driveRobotRelative,
      AutoConstants.holConfig,
      () -> {
        // Boolean supplier that controls when the path will be mirrored for the red alliance
        // This will flip the path being followed to the red side of the field.
        // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
          return alliance.get() == DriverStation.Alliance.Red;
        }
        return false;
      },
      this
    );
  }

  // Odometry class for tracking robot pose
  SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics,
      //Rotation2d.fromDegrees(m_gyro.getAngle()),
      m_gyro.getRotation2d(),
      new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_rearLeft.getPosition(),
          m_rearRight.getPosition()
      });

  static SwerveDriveSubsystem self = null;

  private final Field2d m_field = new Field2d();

  static public SwerveDriveSubsystem getInstance() {
    if (self==null) self = new SwerveDriveSubsystem();
    return self;
  }

  //pass in meters/s and radians/s
  public void setMaxSpeeds(Double drivespeed, Double angularspeed) {
    maximum_drive_speed = drivespeed;
    maximum_rotation_speed = angularspeed;
  }

  public void simulationInit() {
    m_frontLeft.initSimulatonMode();
    m_frontRight.initSimulatonMode();
    m_rearLeft.initSimulatonMode();
    m_rearRight.initSimulatonMode();
  }

  public void simulationPeriodic() {
    if (xSpeedDelivered!=0 || ySpeedDelivered!=0 || rotDelivered!=0) {
        m_frontLeft.simulationPeriodic();
        m_frontRight.simulationPeriodic();
        m_rearLeft.simulationPeriodic();
        m_rearRight.simulationPeriodic();
    }
  }

  @Override
  public void periodic() {
    // Update the odometry
    m_odometry.update(
        //Rotation2d.fromDegrees(m_gyro.getAngle()),
        m_gyro.getRotation2d(),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        });
    m_field.setRobotPose(m_odometry.getPoseMeters());
  }

  public Field2d getField() {
    return m_field;
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }


  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        //Rotation2d.fromDegrees(m_gyro.getAngle()),
        m_gyro.getRotation2d(),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        },
        pose);

      m_lastRecordGyroBeforeRotation = null;
  }

  public Command driveCommand(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rot, boolean fieldRelative, boolean rateLimit) {
    return run(() -> {
      this.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), rot.getAsDouble(), fieldRelative, rateLimit);
    }
    ).withName("swerveDrive");
  }
  
  public Command driveCommand(Double xSpeed, Double ySpeed, Double rot, boolean fieldRelative, boolean rateLimit) {
  return run(() -> {
      this.drive(xSpeed, ySpeed, rot, fieldRelative, rateLimit);
      periodic();
    }
    ).withName("swerveDrive");
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   * @param rateLimit     Whether to enable rate limiting for smoother control.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean rateLimit) {
    double xSpeedCommanded;
    double ySpeedCommanded;
    // ySpeed = 0;
    
    
    if (xSpeed!=0 || ySpeed!=0 || rot!=0)
      System.out.println("SDrive.."+fieldRelative+" xspeed:"+xSpeed+", yspeed:"+ySpeed+", rot:"+rot+ "gyro rot2d="+Rotation2d.fromDegrees(m_gyro.getAngle()));
    

    if (rateLimit) {
      // Convert XY to polar for rate limiting
      double inputTranslationDir = Math.atan2(ySpeed, xSpeed);
      double inputTranslationMag = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));

      // Calculate the direction slew rate based on an estimate of the lateral acceleration
      double directionSlewRate;
      if (m_currentTranslationMag != 0.0) {
        directionSlewRate = Math.abs(DriveConstants.kDirectionSlewRate / m_currentTranslationMag);
      } else {
        directionSlewRate = 500.0; //some high number that means the slew rate is effectively instantaneous
      }
      

      double currentTime = WPIUtilJNI.now() * 1e-6;
      double elapsedTime = currentTime - m_prevTime;
      double angleDif = SwerveUtils.AngleDifference(inputTranslationDir, m_currentTranslationDir);
      if (angleDif < 0.45*Math.PI) {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
      }
      else if (angleDif > 0.85*Math.PI) {
        if (m_currentTranslationMag > 1e-4) { //some small number to avoid floating-point errors with equality checking
          // keep currentTranslationDir unchanged
          m_currentTranslationMag = m_magLimiter.calculate(0.0);
        }
        else {
          m_currentTranslationDir = SwerveUtils.WrapAngle(m_currentTranslationDir + Math.PI);
          m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
        }
      }
      else {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(0.0);
      }
      m_prevTime = currentTime;
      
      xSpeedCommanded = m_currentTranslationMag * Math.cos(m_currentTranslationDir);
      ySpeedCommanded = m_currentTranslationMag * Math.sin(m_currentTranslationDir);
      m_currentRotation = m_rotLimiter.calculate(rot);


    } else {
      xSpeedCommanded = xSpeed;
      ySpeedCommanded = ySpeed;
      m_currentRotation = rot;
    }

    // Convert the commanded speeds into the correct units for the drivetrain
    xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * maximum_drive_speed;
    ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * maximum_drive_speed;
    rotDelivered = m_currentRotation *  DriveConstants.kMaxAngularSpeed * maximum_rotation_speed;


  double xEncoderDelta = xSpeedDelivered;
  double YEncoderDelta = ySpeedDelivered;

  //kp fix some swerve drift due to swerve wheel problems, once hardware is fixed, we may removee this
  Rotation2d targetRotation2d = m_gyro.getRotation2d();

  if (GeneralConstants.kCorrectSwerveDrift) {
    if (Math.abs(rotDelivered) <= 0.0001) {
      if (m_lastRecordGyroBeforeRotation == null) {
        m_lastRecordGyroBeforeRotation = targetRotation2d; //record once after turn
      } else {
        targetRotation2d = m_lastRecordGyroBeforeRotation; //maintain gyro to last recorded state so swerve will correct back if drifted
      }
      rotDelivered = 0.0;
    } else {
      m_lastRecordGyroBeforeRotation = null; //reset
    }
  } 
  


    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, targetRotation2d)
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
    SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond * maximum_drive_speed);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_rearLeft.setDesiredState(swerveModuleStates[2]);
    m_rearRight.setDesiredState(swerveModuleStates[3]);
  }

  /**
   * Sets the wheels into an X formation to prevent movement.
   */
  public void setX() {
    m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, maximum_drive_speed);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_rearLeft.setDesiredState(desiredStates[2]);
    m_rearRight.setDesiredState(desiredStates[3]);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    m_frontLeft.resetEncoders();
    m_rearLeft.resetEncoders();
    m_frontRight.resetEncoders();
    m_rearRight.resetEncoders();
  }

  /** Zeroes the heading of the robot. */
  public void zeroGyro() {
    m_gyro.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    //return Rotation2d.fromDegrees(m_gyro.getAngle()).getDegrees();
    return getPose().getRotation().getDegrees();
  }

  public double getHeadingRadians() {
    return Units.degreesToRadians(getHeading());
  }


  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return m_gyro.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }

  public Command driveTimeCommand(long timeInSec, double xspeed, double yspeed, double rotation, boolean fieldRelative, boolean rateLimit) {
    return driveCommand(xspeed, yspeed, rotation, fieldRelative, rateLimit).withTimeout(timeInSec);
  }

  public void stopModules() {
    drive(0, 0, 0, false, false);
  }

  // Return heading in Rotation2d format
  public Rotation2d getRotation2d(){
    return Rotation2d.fromDegrees(getHeading());
  }

  public ChassisSpeeds getRobotRelativeSpeeds(){
    return DriveConstants.kDriveKinematics.toChassisSpeeds(m_frontLeft.getState(),
                                                           m_frontRight.getState(),
                                                           m_rearLeft.getState(),
                                                           m_rearRight.getState());
  }

  public void driveRobotRelative(ChassisSpeeds speeds){
    this.drive(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond, false, false);
  }
  

  public void displayPosition() {
    Pose2d pose = getPose();

    SmartDashboard.putNumber("RobotPoseX",pose.getX());
    SmartDashboard.putNumber("RobotPoseY",pose.getY());
    SmartDashboard.putNumber("RobotPoseHeading",pose.getRotation().getDegrees());

  }
  

}