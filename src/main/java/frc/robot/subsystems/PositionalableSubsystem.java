package frc.robot.subsystems;

public interface PositionalableSubsystem {
    void moveToPosition(double poistion);
    boolean isAtPosition(double poistion);
    void stop();
}
