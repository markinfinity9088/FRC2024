package frc.utils;

import edu.wpi.first.math.util.Units;

public class ArmPreset {
    public final double elbowAngleRadians;
    public final double wristAngleRadians;

    public ArmPreset(double elbowAngleDegrees, double wristAngleDegrees){
        this.elbowAngleRadians = Units.degreesToRadians(elbowAngleDegrees);
        this.wristAngleRadians = Units.degreesToRadians(wristAngleDegrees);
    }
}
