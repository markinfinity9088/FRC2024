package frc.robot.commands;

import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.Constants.DriveConstants;
import java.util.function.Supplier;

import javax.swing.text.Utilities;

import org.opencv.core.Mat;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveSampleMoveCommand extends Command {

private boolean finished;
  private boolean xflag;
  private boolean yflag;
  SwerveDriveSubsystem swerveSubsystem;
  Supplier<Boolean> switchOverride;
  Supplier<Double> headingFunction, setpointFunction;

  private PIDController vXController, vYController;
  private ProfiledPIDController thetaController;

  private double vX;
  private double vY;

  private double xSetpoint;
  private double ySetpoint;
  private double tSetpoint;

  private boolean reset;
  private double tolerance;

  private double maxdrivespeed;
  private double maxturnspeed;


  private Pose2d resetPose;
  private boolean m_rotateOnly;

  private double m_degreeTolerance=3;

  private boolean m_driveOnly = false;

  private boolean m_debug = true;

    // tSetpoint is in radians and posive for counter clockwise
    //call this constructor for rotate only
  public SwerveSampleMoveCommand(SwerveDriveSubsystem swerveSubsystem,
      double tSetpoint, boolean reset, Pose2d resetPose,
      double degreeTolerance) 
  {
    this(swerveSubsystem, swerveSubsystem.getPose().getX(), swerveSubsystem.getPose().getY(), tSetpoint, reset,
        resetPose, 0.3);
    if (reset) {
      this.xSetpoint = this.ySetpoint = 0;
    }
    m_rotateOnly = true;

    m_degreeTolerance = degreeTolerance;

  }

  //drive only, no turning
  public SwerveSampleMoveCommand(SwerveDriveSubsystem swerveSubsystem,
      double xSetpoint, double ySetpoint, double tolerance) 
  {
    this(swerveSubsystem, xSetpoint, ySetpoint, 0, false,
        null, tolerance);
    m_driveOnly = true;


  }


  // Command constructor
  // x/y setpoint is in meters, forward is +ve for x and left is +ve for y
  // tSetpoint is in radians and posive for counter clockwise
  // If you want to reset pose then set reset to true and pass pose for resetPose
  // tolerance tells how much accurate the tolerance to distance.
  public SwerveSampleMoveCommand(SwerveDriveSubsystem swerveSubsystem,
         double xSetpoint, double ySetpoint, double tSetpoint, boolean reset, Pose2d resetPose,
         double tolerance)
  {
     m_rotateOnly = false;
     this.reset = reset;
     this.resetPose = resetPose;

    addRequirements(swerveSubsystem);

    this.tolerance = tolerance;


    this.swerveSubsystem = swerveSubsystem;

    this.tSetpoint = tSetpoint;
    this.xSetpoint = xSetpoint;
    this.ySetpoint = ySetpoint;

    
    vX = 0;
    vY = 0;

    finished = false;

    maxdrivespeed = DriveConstants.kMaxSpeedMetersPerSecond;
    maxturnspeed = DriveConstants.kMaxAngularSpeed;

  }

  public void setMaxSpeeds(double drivespeed, double turnspeed) {
    maxdrivespeed = drivespeed;
    maxturnspeed = turnspeed;
  }

  @Override
  public void initialize() {
  
    if(reset){ swerveSubsystem.resetOdometry(resetPose);}

    finished = false;
    xflag = false;
    yflag = false;

    //vXController = new PIDController(6, 0.006, 0.008);
    //vYController = new PIDController(6, 0.006, 0.008);

    //change value of kp to control max speed
    vXController = new PIDController(1, 0.006, 0.008);
    vYController = new PIDController(1, 0.006, 0.008);


    //thetaController = new PIDController(0.1, 0.004, 0.02);
    //thetaController = new PIDController(0.6, 0.004, 0.001);
    //thetaController = new PIDController(2.5, 0.004, 0.008);
    thetaController = new ProfiledPIDController(2.2,0,0,new Constraints(5,10));


    //thetaController.enableContinuousInput(0, 360);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);
    //thetaController.disableContinuousInput();

  }

  boolean didReachXYSetPoint() {
    double currentPoseY = swerveSubsystem.getPose().getY();
    double currentPoseX = swerveSubsystem.getPose().getX();

    if (m_debug) {
      SmartDashboard.putNumber("YDIFF",Math.abs(currentPoseY-ySetpoint));
      SmartDashboard.putNumber("XDIFF",Math.abs(currentPoseX-xSetpoint));

    }
    
    if (Math.abs(currentPoseY-ySetpoint) <= tolerance && 
        Math.abs(currentPoseX - xSetpoint) <= tolerance
      ) {
        return true;
      }
    return false;
  }


  double getAngleDifference() {
    double currentPoseT = swerveSubsystem.getHeading();
    double tsetpointdegrees = Units.radiansToDegrees(tSetpoint);
    double headingDifference = Math.abs(currentPoseT - tsetpointdegrees);

    
    return headingDifference;
  }

  boolean didReachThetaSetpoint(){
    if (m_driveOnly) {
      return true;
    }
    double deltaAngle = getAngleDifference();

    if (m_debug) {
      SmartDashboard.putNumber("TSetpoint", Units.radiansToDegrees(tSetpoint));
      SmartDashboard.putNumber("AngleDiff", deltaAngle);
    }
    

    if (Math.abs(deltaAngle) <= m_degreeTolerance) {
      return true;
    }
    return false;

  }

  @Override
  public void execute(){
      

      double turningSpeed;

      turningSpeed = thetaController.calculate(Units.degreesToRadians(swerveSubsystem.getHeading()), tSetpoint);

      System.out.println("Swervemove heading="+Units.degreesToRadians(swerveSubsystem.getHeading())+" tsetpoint="+tSetpoint+" turningspeed="+turningSpeed);

      turningSpeed = (turningSpeed > maxturnspeed)?maxturnspeed:turningSpeed;

      turningSpeed = Math.abs(turningSpeed) > 0.05 ? turningSpeed : 0.0;

      if (m_debug) {
        SmartDashboard.putNumber("ROT CACL", turningSpeed);
        SmartDashboard.putNumber("ODO Y", swerveSubsystem.getPose().getY());
        SmartDashboard.putNumber("ODO X", swerveSubsystem.getPose().getX());
        SmartDashboard.putNumber("ROBO DEG", swerveSubsystem.getHeading());
        SmartDashboard.putBoolean("ISFinished", finished);
      }
      

      vX = vXController.calculate(swerveSubsystem.getPose().getX(), xSetpoint); // X-Axis PID
      vY = vYController.calculate(swerveSubsystem.getPose().getY(), ySetpoint); // Y-Axis PID

      //KP hack reset vX , vY to zero for rotate only
      if (m_rotateOnly) {
        vX = 0;
        vY = 0;
      }

      if (m_driveOnly) {
        turningSpeed = 0;
      }

      
      vX = MathUtil.clamp(vX, -maxdrivespeed, maxdrivespeed);
      vY = MathUtil.clamp(vY, -maxdrivespeed, maxdrivespeed);
      turningSpeed = MathUtil.clamp(turningSpeed, -maxturnspeed, maxturnspeed);

      if (m_debug) {
        SmartDashboard.putNumber("vX", vX);
        SmartDashboard.putNumber("vY", vY);
        SmartDashboard.putNumber("vTurn", turningSpeed);

      }
      
      
      swerveSubsystem.drive(vX, vY, turningSpeed, true, true);

  }

  // Stop all module motor movement when command ends
  @Override
  public void end(boolean interrupted){
    if (m_debug) {
      System.out.println("End SwerveSampleMoveCommand called");
    }
    swerveSubsystem.stopModules();
  }

  @Override
  public boolean isFinished(){
    if (didReachXYSetPoint() &&didReachThetaSetpoint()) {
        finished = true;
      }
    return finished;
  }
  
}
