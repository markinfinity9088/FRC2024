// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
//import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.AutoConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class DriveConstants {
    public static final int kLeftMotor1Port = 17;
    public static final int kLeftMotor2Port = 18;
    public static final int kRightMotor1Port = 19;
    public static final int kRightMotor2Port = 20;

    // KP revisit this later, using this to correct swerve field relative work where
    // it is going in reverse after we turn robot
    public static final boolean kInverseGyroAngle = true;

    public static final String driveType = "SWERVE"; // DIFFER or SWERVE

    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.8;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(20.0);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(20.0);
    // Distance between front and back wheels on robot
    public static final double kTrackRadius = Math.sqrt(Math.pow(kTrackWidth, 2) + Math.pow(kWheelBase, 2));
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    // public static final double kBackRightChassisAngularOffset = Math.PI / 2;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;


    // SPARK MAX CAN IDs
    // using intake as front
    public static final int kFrontLeftDrivingCanId = 6;
    public static final int kRearLeftDrivingCanId = 4;
    public static final int kFrontRightDrivingCanId = 8;
    public static final int kRearRightDrivingCanId = 2;

    public static final int kFrontLeftTurningCanId = 5;
    public static final int kRearLeftTurningCanId = 3;
    public static final int kFrontRightTurningCanId = 7;
    public static final int kRearRightTurningCanId = 1;

    public static final boolean kGyroReversed = false;

    public static final double kMaxDriveSubsystemSpeed = 1; // 0-1
    public static final double kMaxDriveSubsystemTurnSpeed = 1; // 0-1

  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T,
    // 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth
    // will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite
    // direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = Units.inchesToMeters(3.0);;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15
    // teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingPFr = 0.04;
    public static final double kDrivingIFr = 0;
    public static final double kDrivingDFr = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    public static final double kDrivingPFl = 0.04;
    public static final double kDrivingIFl = 0;
    public static final double kDrivingDFl = 0;

    public static final double kDrivingPBl = 0.04;
    public static final double kDrivingIBl = 0;
    public static final double kDrivingDBl = 0;

    public static final double kDrivingPBr = 0.04;
    public static final double kDrivingIBr = 0;
    public static final double kDrivingDBr = 0;

    public static final double kTurningP = 1; //1
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class IntakeConstants {
    public static final int elbowCanId = 13;
    public static final int CURRENT_LIMIT_A = 42220;
    public static final double MAX_SPEED = 1.0;
    public static final double ELBOW_MAX_SPEED = 0.5;
    public static final int intakeCanId = 17;
    public static final int intakeWristCanId = 16;

    public static final double wristP = 0.1;
    public static final double wristI = 0;
    public static final double wristD = 0;
    public static final int beamBreakDIO = 0;
  }

  public static final class ShooterConstants {
    // public static final int frontRightShooterCanId = 13;
    // public static final int backRightShooterCanId = 12;
    // public static final int frontLeftShooterCanId = 15;
    // public static final int backLeftShooterCanId = 14;
    public static final int rightShooterCanId = 15;
    public static final int leftShooterCanId = 12;
    public static final int shooterPivotCanId = 11;
    public static final int CURRENT_LIMIT_A = 30;
  }

  public static final class ElevatorConstants {
    public static final int CURRENT_LIMIT_A = 30;
    public static final double MAX_SPEED = 0.5;
    public static final int elbowFrontCanId = 18;
    public static final int elbowBackCanId = 19;
    public static final int elevatorCanId = 20;
  }

  public static final class PulleyConstants {
    public static final String PULLEY_RANGE_LABEL = "Pulley Range";
    public static final String PULLEY_LOW_LIMIT = "Low Pulley Limit";
    public static final int Pulley_RtCanId = 10;
    public static final int Pulley_LtCanId = 11;
  }

  public static final class ClimbConstants {
    public static final double extenisonLimit = 2; // 2 feet
    public static final int leftClimbCanId = 10;
    public static final int rightClimbCanId = 9;
    public static final double MAX_SPEED = 0.7;
    public static final int climbCurrentLimit = 40;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 1;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 3;
    public static final double kPYController = 3;
    public static final double kPThetaController = .1;

    public static final String traj1 = "";

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);

    public static final HolonomicPathFollowerConfig holConfig = new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                                                                  // new PIDConstants(3.425,0.03,0.13), // Translation PID constant 4,.1,0 | 3.8,.01,0
                                                                  // new PIDConstants(1,0.0,0.0), // Rotation PID constants 2.35,.01,0 | 2.35,.01,0
                                                                  new PIDConstants(3,0.0,0.03), // Translation PID constant 4,.1,0 | 3.8,.01,0
                                                                  new PIDConstants(3.6,0.8,0.27),// Rotation PID constants 2.35,.01,0 | 2.35,.01,0
                                                                  // new PIDConstants(2.7,0.1,0.08), // Translation PID constant
                                                                  // new PIDConstants(3,0.2,0.06), // Rotation PID constants
                                                                   DriveConstants.kMaxSpeedMetersPerSecond, // Max module speed, in m/s
                                                                  DriveConstants.kTrackRadius, // Drive base radius in meters. Distance from robot center to furthest module.
                                                                  new ReplanningConfig() // Default path replanning config. See the API for the options here
                                                                );
  }

  public static final class OIConstants {
    public static final String controllerType = "PS4"; // PS4 or XBox or PS4s
    public static final int kDriverControllerPort0 = 0;
    public static final int kDriverControllerPort1 = 1;  
    public static final double kDriveDeadband = 0.05;
  }

  public static final class AutoConstantsPathPlanner {
    //x, y controller
    public static final double kTranslationControllerP = 3.0;
    public static final double kTranslationControllerD = 0.05;
    public static final PIDConstants kTranslationControllerConstants = 
      new PIDConstants(AutoConstantsPathPlanner.kTranslationControllerP, 0.0, AutoConstantsPathPlanner.kTranslationControllerD);

    //theta controller
    public static final double kThetaControllerP = 1.5;
    public static final double kThetaControllerD = 0.05;
    public static final PIDConstants kThetaControllerConstants = 
      new PIDConstants(AutoConstantsPathPlanner.kThetaControllerP, 0.0, AutoConstantsPathPlanner.kThetaControllerD);
  }


  public static final class GeneralConstants {
    public static final boolean   kInVerboseMode = true; //set to false during competition
    public static final boolean kCorrectSwerveDrift=false; //kp we will use drift correction logic if this flag is on
  }
}
