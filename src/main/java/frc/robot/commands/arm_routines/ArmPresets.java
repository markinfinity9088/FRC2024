package frc.robot.commands.arm_routines;

import frc.robot.commands.arm_routines.logic.ArmPositioningInfo;
import frc.robot.commands.arm_routines.logic.ArmRoutine;

//Place to keep all your preset routine positions
public class ArmPresets {
    public static final ArmRoutine Handoff = createHandoffPreset();
    public static final ArmRoutine Stow = createStowPreset();
    public static final ArmRoutine PickupRing = createPickupRingPreset();
    public static final ArmRoutine AmpDropOff = createAmpDropOffPreset();

    public static final ArmRoutine TestRoutine = createTestingPreset();



    private static ArmRoutine createTestingPreset() {
         ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
            new ArmPositioningInfo(null, Long.valueOf(3200), null),
            new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-200), Long.valueOf(4100), null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    //Change values in below routines to fine tune behaviors of presets
    //// creations of routines 

    private static  ArmRoutine createHandoffPreset() {
        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(null, null, null),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(null, null, null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    private static ArmRoutine createAmpDropOffPreset() {
        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(null, null, null),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(null, null, null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    private static ArmRoutine createPickupRingPreset() {
         ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(Long.valueOf(50), null, null),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(100), Long.valueOf(250), null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    private static ArmRoutine createStowPreset() {
         ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(null, null, null),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(null, null, null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


}
