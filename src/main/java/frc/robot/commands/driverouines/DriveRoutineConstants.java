package frc.robot.commands.driverouines;

import edu.wpi.first.math.geometry.Pose2d;

public class DriveRoutineConstants {
    public static final double StartY = 3; // meters to left from origin in center
    public static final double PickupX = 10; //meters towards pickup station from origin

    public static final double distanceToShootFromPickup = 5; //meters

    public static final double PickupHeading = 0; 
    public static final double ShootHeading = 175; //degrees , it changes sign from 179 to -179

    public static final double WaitTimeB4CameraDetectionSeconds = 0.5; //seconds

    public static final boolean reset_pose_with_camera = true;
}
