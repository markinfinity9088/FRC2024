package frc.robot.vision.limelight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class LimeLightFacade {

	// different from pipeline
	private String limelightName = "limelight";
	private double kCameraHeight = 9.14;
	private double kMountingAngle = 35.0;
	private double GoalHeight = 24.5; // inches, deg
	private static LimeLightFacade self;

	public LimeLightFacade() {
	}

	public static LimeLightFacade getInstance(){
		if (self == null){
			self = new LimeLightFacade();
		}
		return self;
	}

	public LimeLightFacade(String name) {
		limelightName = name;
	}

	public double getDistanceToGoalInches() {
		return (GoalHeight - kCameraHeight)
				/ Math.tan(Units.degreesToRadians(kMountingAngle + getYAngleOffsetDegrees()));
	}

	public void setGoalHeight(double GoalHeight) {
		this.GoalHeight = GoalHeight;
	}

	public double getGoalHeight() {
		return GoalHeight;
	}

	public double getDistanceToGoalMeters() {
		return getRSpace3d().getZ();
	}

	public double getYAngleOffsetDegrees() {
		return LimelightHelpers.getTY(limelightName);
	}

	public double getXAngleOffsetDegrees() {
		return -1 * LimelightHelpers.getTX(limelightName); // must be negative
	}

	public double getXOffsetRadians() {
		return Units.degreesToRadians(getXAngleOffsetDegrees());
	}

	public boolean isTargetVisible() {
		return LimelightHelpers.getTV(limelightName);
	}

	public void setLED(boolean lightOn) {
		if (lightOn)
			LimelightHelpers.setLEDMode_ForceOn(limelightName); // LED force on
		else
			LimelightHelpers.setLEDMode_ForceOff(limelightName); // LED force off
	}

	// Back Limelight
	public void setRetroPipeline() {
		setGoalHeight(LimelightConstants.kMiddleRetroTapeHeight);
		LimelightHelpers.setPipelineIndex(limelightName, 0);
	}

	public void setAprilTagPipeline() {
		LimelightHelpers.setPipelineIndex(limelightName, 1);
	}

	public void setAprilTagFarPipeline() {
		LimelightHelpers.setPipelineIndex(limelightName, 2);
	}

	public Command setLEDCommand(boolean lightOn) {
		return new InstantCommand(() -> setLED(lightOn));
	}

	public Pose2d getBotPose2d() {
		return LimelightHelpers.getBotPose2d(limelightName);
	}

	public Pose3d getRSpace3d() {
		return LimelightHelpers.getTargetPose3d_RobotSpace(limelightName);
	}

	public double getTX(){
		return getRSpace3d().getX();
	}

	public void updateDashboard() {
		double distance = -9999;
		if (isTargetVisible()) {
			distance = getDistanceToGoalMeters();
		}
		Pose3d r_Pose3d = getRSpace3d();
		SmartDashboard.putNumber("ll distance to goal", distance);
		SmartDashboard.putNumber("tz", r_Pose3d.getZ());

		Pose2d botpose = getBotPose2d();
		SmartDashboard.putBoolean("CameraTargetIsDetected", isTargetVisible());
		SmartDashboard.putNumber("CameraBasedPoseX", botpose.getX());
		SmartDashboard.putNumber("CameraBasedPoseY", botpose.getY());
		SmartDashboard.putNumber("CameraBaseBotHeading", botpose.getRotation().getDegrees());
	}
}