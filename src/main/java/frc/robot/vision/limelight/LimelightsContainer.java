package frc.robot.vision.limelight;

import java.util.HashMap;

public class LimelightsContainer {

    private static LimelightsContainer instance;
    private HashMap<String, LimeLightFacade> m_limelights;

    private LimelightsContainer() {
        m_limelights = new HashMap<>();
    }

    public static LimelightsContainer getInstance() {
        if (instance == null) {
            instance = new LimelightsContainer();
        }
        return instance;
    }

    public void addLimeLight(String name, LimeLightFacade limelight) {
        m_limelights.put(name, limelight);
    }

    public LimeLightFacade getLimeLight(String name) {
        return m_limelights.get(name);
    }
}
