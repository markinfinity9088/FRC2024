package frc.robot.commands.driverouines;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.utils.PID.PIDValue;

public class BaseMoveCommand extends Command {

    double m_deltaX;
    double m_deltaY;
    double m_deltaTurnDegrees;

    double m_finalX;
    double m_finalY;

    double maxspeed = 2;

    PIDValue rotationPID = new PIDValue(3.6, 0.8, 0.27);
    PIDValue translationPID = new PIDValue(3, 0.0, 0.03);

    ProfiledPIDController thetaController ;
    ProfiledPIDController xController;
    ProfiledPIDController yController;

    double startHeading;
    SwerveDriveSubsystem sdrive;
   

    public BaseMoveCommand(double deltaX, double deltaY, double deltaTurnDegrees, double maxTranslationSpeed, double maxTurnSpeed, double translationTolerance, double rotationToleranceDegrees) {
        m_deltaX = deltaX;
        m_deltaY = deltaY;
        m_deltaTurnDegrees = deltaTurnDegrees;

        addRequirements(SwerveDriveSubsystem.getInstance());

        thetaController = new ProfiledPIDController(rotationPID.kP,rotationPID.kI,rotationPID.kD,new Constraints(maxTurnSpeed,10));
        thetaController.setTolerance(Units.degreesToRadians(rotationToleranceDegrees),0.01);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        xController = new ProfiledPIDController(translationPID.kP, translationPID.kI, translationPID.kD, new Constraints(maxTranslationSpeed, 10));
        yController = new ProfiledPIDController(translationPID.kP, translationPID.kI, translationPID.kD, new Constraints(maxTranslationSpeed, 10));
    
    }

    @Override
    public void initialize() {
        sdrive = SwerveDriveSubsystem.getInstance();
        Pose2d startPose = sdrive.getPose();
        m_finalX = startPose.getX() + m_deltaX;
        m_finalY = startPose.getY() + m_deltaY;

        this.startHeading = sdrive.getHeadingRadians();

        thetaController.setGoal(Units.degreesToRadians(m_deltaTurnDegrees));
        xController.setGoal(m_finalX);
        yController.setGoal(m_finalY);

    }

     // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Pose2d pose = sdrive.getPose();
        sdrive.drive(xController.calculate(pose.getX()), yController.calculate(pose.getY()), thetaController.calculate(sdrive.getHeadingRadians()-startHeading), false, false);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        sdrive.drive(0, 0, 0, false, false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return thetaController.atGoal() && xController.atGoal() && yController.atGoal();
    }
}
    

