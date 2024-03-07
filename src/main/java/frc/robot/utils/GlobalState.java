package frc.robot.utils;


/*
 * Maintains last used preset name and prevents some presets based on last used preset, you can override this by clearning last used preset
 */

public class GlobalState {
    
    private static GlobalState instance = null;

    private String previousPresetUsed = "";

    private AllianceSideEnum allianceside;

    private Double maxSwerveSpeed = 1.0;

    private GlobalState() {

    }

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public void setPreviousPresetRun(String presetName) {
        previousPresetUsed = presetName;
    }

    public String getPreviousPresetRun() {
        return previousPresetUsed;
    }

    public boolean isAllowedToRunPreset(String presetName) {

        if (previousPresetUsed.equals("")) {
            return true;
        } 

        if (presetName.equals("Stow")) {
            if (previousPresetUsed.equals("Stow") || previousPresetUsed.equals("AmpDrop") 
                || previousPresetUsed.equals("StowFromAmp")) 
            {
                return false;
            }

        } else if(presetName.equals("StowFromAmp")) {
            if (!previousPresetUsed.equals("AmpDrop") ) 
            {
                return false;
            }

        }
        else if (presetName.equals("Pickup")) {
             if (!previousPresetUsed.equals("StowFromAmp") && !previousPresetUsed.equals("Stow") ) 
            {
                return false;
            }

        } else if (presetName.equals("Handoff")) {
            if (!previousPresetUsed.equals("Stow") ) 
            {
                return false;
            }
        }

        return true;
    }

    void setAllianceSide(AllianceSideEnum side) {
        allianceside = side;
    }

    AllianceSideEnum getAllianceSide() {
        return allianceside;
    }

    public void toggleMaxSpeed() {
        if (maxSwerveSpeed > 0.4) {
            maxSwerveSpeed = 0.4;
        } else {
            maxSwerveSpeed = 1.0;
        }
    }

    public Double getMaxSpeed() {
        return maxSwerveSpeed;
    }

}
