package frc.robot.utils;

import edu.wpi.first.math.util.Units;

public class PivotComputation {
	private double height = 2.00 - .56; // height of speaker - height of pivot from ground, cm
	private double centerDistance = .184; // pivot to center, cm

	public PivotComputation() {
	}

	public PivotComputation(double height, double centerDistance) {
		this.height = height;
        this.centerDistance = centerDistance;
	}

    public double getPivotAngle(double distance) {
        double pivotDistance = distance - centerDistance;
        return Units.radiansToDegrees(Math.atan(height/pivotDistance));
    }
}
