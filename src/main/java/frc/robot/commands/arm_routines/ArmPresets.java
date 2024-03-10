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

    public static final ArmRoutine PivotShootTilt = createPivotUpPreset();
    public static final ArmRoutine PivotDropTilt = createPivotLowPreset();

    public static final long PIVOT_MAX_TILT = 155;
    public static final long PIVOT_SHOOT_POINT = 155;
    public static final long PIVOT_TOLERANCE = 5;
    public static final long PIVOT_MIN_POINT = 2;

    public static final long WRIST_HANDOFF_POSITION = 4250;




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
        System.out.println("Handoff preset created "+WRIST_HANDOFF_POSITION);
        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(null, null, null)
        };

       // ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-15), Long.valueOf(4250), null);
        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-15), Long.valueOf(WRIST_HANDOFF_POSITION), null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }

    //wrist, elbow, elevator
    private static ArmRoutine createAmpDropOffPreset() {
        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           new ArmPositioningInfo(null, Long.valueOf(3180), null),
           new ArmPositioningInfo(Long.valueOf(-70), null, null),
           new ArmPositioningInfo(null, null, Long.valueOf(21000))
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-167), Long.valueOf(3150), Long.valueOf(21000));

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    private static ArmRoutine createPickupRingPreset() {
         ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           new ArmPositioningInfo(null, Long.valueOf(3800), null,50),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-390), Long.valueOf(4250), null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    private static ArmRoutine createStowPreset() {
         ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
            // new ArmPositioningInfo(null, null, Long.valueOf(1000)),
             new ArmPositioningInfo(Long.valueOf(-100), null, null),
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(Long.valueOf(-15), Long.valueOf(2950), null);

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


    

    private static  ArmRoutine createPivotUpPreset() {
        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           // new ArmPositioningInfo(null, null, null)
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(null, null, null);
        finalPosition.setPivotPosition(Long.valueOf(PIVOT_SHOOT_POINT)); //TODO set correct value later
        //finalPosition.setShooterOn();

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }

    private static  ArmRoutine createPivotLowPreset() {
        ArmPositioningInfo lowMovePosition = new ArmPositioningInfo(null, null, null);
        lowMovePosition.setPivotPosition(Long.valueOf(PIVOT_MIN_POINT)); //TODO set correct value later


        ArmPositioningInfo initialSequentialPositions[] = new ArmPositioningInfo[] {
           lowMovePosition
        };

        ArmPositioningInfo finalPosition = new ArmPositioningInfo(null, null, null);
        //finalPosition.setPivotPosition(PIVOT_MIN_POINT); //TODO set correct value later

        ArmRoutine routine = new ArmRoutine(initialSequentialPositions, finalPosition);

        return routine;
    }


}
