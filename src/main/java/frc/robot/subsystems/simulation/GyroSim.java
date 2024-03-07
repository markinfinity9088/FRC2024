package frc.robot.subsystems.simulation;

public class GyroSim {

    private double rate;
    private double angle;
    private double yaw;
    private double roll;
    private double pitch;

    public GyroSim() {

    }

    public void reset() {
        rate = angle = yaw = roll = pitch = 0.0;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getRate() {
        return rate;
    }

    public double getAngle() {
        return angle;
    }

    public double getYaw() {
        return angle;
    }

    public double getRoll() {
        return roll;
    }

    public double getPitch() {
        return pitch;
    }
}
