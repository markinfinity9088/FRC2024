package frc.robot.utils;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AprilTagUtils {
    public static void startDetector() {
        //Start thread to detect april tags
        Thread visionThread = new Thread(() -> apriltagVisionThreadProc());
        visionThread.setDaemon(true);
        visionThread.start();
    }
    private static void apriltagVisionThreadProc() {
        AprilTagDetector detector = new AprilTagDetector();
        detector.addFamily("tag16h5", 0);

        // Get the UsbCamera from CameraServer
        UsbCamera camera = CameraServer.startAutomaticCapture();
        // Set the resolution
        camera.setResolution(640, 480);

        // Get a CvSink. This will capture Mats from the camera
        CvSink cvSink = CameraServer.getVideo();
        // Setup a CvSource. This will send images back to the Dashboard
        CvSource outputStream = CameraServer.putVideo("detect", 640, 480);

        // Mats are very memory expensive. Lets reuse this Mat.
        Mat mat = new Mat();
        Mat grayMat = new Mat();
        ArrayList<Integer> tags = new ArrayList<>();

        //
        Scalar outlineColor = new Scalar(0, 255, 0);
        Scalar xColor = new Scalar(0, 0, 255);

        // This cannot be 'true'. The program will never exit if it is. This
        // lets the robot stop this thread when restarting robot code or
        // deploying.
        while (!Thread.interrupted()) {
            // Tell the CvSink to grab a frame from the camera and put it
            // in the source mat.  If there is an error notify the output.
            if (cvSink.grabFrame(mat) == 0) {
              // Send the output the error.
              outputStream.notifyError(cvSink.getError());
              // skip the rest of the current iteration
              continue;
            }

            Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_RGB2GRAY);

            AprilTagDetection[] detections = detector.detect(grayMat);
            tags.clear();
            for (AprilTagDetection detection : detections) {
              tags.add(detection.getId());

              for (var i = 0; i <= 3; i++) {
                var j = (i + 1) % 4;
                var pt1 = new Point(detection.getCornerX(i), detection.getCornerY(i));
                var pt2 = new Point(detection.getCornerX(j), detection.getCornerY(j));
                Imgproc.line(mat, pt1, pt2, outlineColor, 2);
              }

              var cx = detection.getCenterX();
              var cy = detection.getCenterY();
              var ll = 10;
              Imgproc.line(mat, new Point(cx - ll, cy), new Point(cx + ll, cy), xColor, 2);
              Imgproc.line(mat, new Point(cx, cy - ll), new Point(cx, cy + ll), xColor, 2);
              Imgproc.putText(mat, Integer.toString(detection.getId()), new Point (cx + ll, cy), Imgproc.FONT_HERSHEY_SIMPLEX, 1, xColor, 3);
            }

            SmartDashboard.putString("tag", tags.toString());
            // Give the output stream a new image to display
            outputStream.putFrame(mat);
          }

        detector.close();
  }
    
}
