package frc.robot.utils;

import edu.wpi.first.math.util.Units;

public class PivotComputation {
	private double height = 2.40 - .56; // height of speaker - height of pivot from ground, cm
	private double centerDistance = .184; // pivot to center, cm

	public PivotComputation() {
	}

	public PivotComputation(double height, double centerDistance) {
		this.height = height;
        this.centerDistance = centerDistance;
	}

    // public double getPivotAngle(double distance) {
    //     double pivotDistance = distance - centerDistance;
	// 	// if (distance > 3){
	// 	// 	pivotDistance *= .9;
	// 	// }
    //     return Units.radiansToDegrees(Math.atan(height/pivotDistance));
    // }

	public double getPivotAngle(double distance){
		return 157.75 - (105*distance) + (34.7*Math.pow(distance, 2)) - (5.36*Math.pow(distance, 3)) + (0.315*Math.pow(distance, 4));
		// return 127 - (64.9*distance) + (15*Math.pow(distance, 2)) - (1.2*Math.pow(distance, 3));
		// return 59.8*Math.pow(distance, -.524);
	}
}
