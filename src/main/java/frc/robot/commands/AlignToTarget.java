package frc.robot.commands;


import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.vision.limelight.LimeLightFacade;

public class AlignToTarget extends Command {
  private SwerveDriveSubsystem m_drivetrain;
  private LimeLightFacade m_limelight;

  private ProfiledPIDController xController;
  private ProfiledPIDController yController;
  private ProfiledPIDController thetaController;

  private double kPXControllerCube = 0.6;
  private double kPYControllerCube = 1.0;

  private double kPThetaController = 1.5;

  private double xControllerGoalCube = 0.45;

  //private double thetaControllerkP

  /** Creates a new SmoothAlign. */
  public AlignToTarget (LimeLightFacade limelight) {
    this.m_drivetrain = SwerveDriveSubsystem.getInstance();
    this.m_limelight = limelight;

    // Use addRequirements() here to declare subsystem dependencies.
    xController = new ProfiledPIDController(kPXControllerCube, 0, 0, new Constraints(0.2, 0.2));
    yController = new ProfiledPIDController(kPYControllerCube, 0, 0, new Constraints(3, 3));
    thetaController = new ProfiledPIDController(kPThetaController, 0, 0, new Constraints(5, 10));
    
    addRequirements(m_drivetrain);

    xController.setGoal(xControllerGoalCube);
    xController.setTolerance(0,0);

    yController.setGoal(0);
    yController.setTolerance(Units.degreesToRadians(0),0);

    thetaController.setGoal(0);
    thetaController.setTolerance(Units.degreesToRadians(0),0);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_limelight.setAprilTagPipeline();
    // m_limelight.setLED(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_limelight.isTargetVisible()) {
      m_drivetrain.drive(
        xController.calculate(m_limelight.getDistanceToGoalMeters()), 
        yController.calculate(m_limelight.getXOffsetRadians()), 
        thetaController.calculate(m_drivetrain.getHeadingRadians()), 
        true,
        false);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // m_limelight.setLED(false);
    m_drivetrain.drive(0, 0, 0, true, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return xController.atGoal() && yController.atGoal() && thetaController.atGoal();
  }
}
