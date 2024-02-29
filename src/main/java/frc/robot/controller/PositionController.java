package frc.robot.controller;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PositionController {
    static final String WRIST_AMP_KEY="WristAmp";
    static long wristAmpPosition = 20;
    static final String ELBOW_AMP_KEY="ElbowAmp";
    static long elbowAmpPosition = 20;
    static final String ELEV_AMP_KEY="ElevAmp";
    static long elevAmpPosition = 20;
    
    ShuffleboardTab ptab;
    static PositionController self;

    public PositionController() {
       ptab = Shuffleboard.getTab("Positions");
       ptab.add(WRIST_AMP_KEY, wristAmpPosition);
       ptab.add(ELBOW_AMP_KEY, elbowAmpPosition);
       ptab.add(ELEV_AMP_KEY, elevAmpPosition);
    }

    public static PositionController getInstance() {
        return self==null? self=new PositionController():self;
    }

    public void refresh() {
        wristAmpPosition = (long)SmartDashboard.getNumber(WRIST_AMP_KEY, wristAmpPosition);
        elbowAmpPosition = (long)SmartDashboard.getNumber(ELBOW_AMP_KEY, elbowAmpPosition);
        elevAmpPosition = (long)SmartDashboard.getNumber(ELEV_AMP_KEY, elevAmpPosition);
    }
}
