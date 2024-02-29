package frc.robot.vision.limelight;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public final class LimelightConstants {
    public static final Pose3d kBackLimelightPose = 
      new Pose3d(
        new Translation3d(15.75, 9.14, 0.0), //inches
        new Rotation3d(0.0, 35.0, 0.0));//degrees

    public static final double kMiddleRetroTapeHeight = 24.5; // inches
    public static final double kCubeLowHeight = -18; // inches
    public static final double kCubeMiddleHeight = 5.5; // inches
    public static final double kCubeHighHeight = 17; // inches
  } 