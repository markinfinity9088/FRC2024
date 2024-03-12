package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.commands.SwerveSampleMoveCommand;

/** Returns a command that grabs the item */
public class IntakeCommands {
  final static long wristIntakePosition = 20;
  final static long elbowIntakePosition = 100;
  final static long elevatorIntakePosition = 50;

  final static long wristHandoffPosition = 3500;
  
  final static long pivotShootPosition = 30;

  final static  long elbowSecurePosition = 250;

  final static long elbowAMPPosition = 10;
  final static long elevatorAMPPosition = 10;
  final static long pulleyAMPPosition = 10;

  public static Command moveToIntakePos(){
    return ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PickupRing);
  }

  public static Command handoffAndShootCommand() {
    //Handoff
    ParallelCommandGroup pcommandGroup1 = new ParallelCommandGroup();

    pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    // pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    pcommandGroup1.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}));

    //shoot
    ParallelCommandGroup pcommandGroup2 = new ParallelCommandGroup();

    //pcommandGroup2.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    pcommandGroup2.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1));
    pcommandGroup2.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(0.5));


    SequentialCommandGroup commandGroup = new SequentialCommandGroup();
    commandGroup.addCommands(pcommandGroup1.withTimeout(2));
    commandGroup.addCommands(pcommandGroup2.withTimeout(1));
    commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().stop();}).withTimeout(.1));
    commandGroup.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().stopShooterWheels();}).withTimeout(.1));

    return commandGroup;
  }

  public static Command rightAutonOneRingRed() {
    SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
    s_drive.setMaxSpeeds(0.5, 0.2);

     //Move
    ParallelCommandGroup pcommandGroup0 = new ParallelCommandGroup();

    pcommandGroup0.addCommands(new SwerveSampleMoveCommand(s_drive, 50,0, 0, true, new Pose2d(), 0.05));

    ParallelCommandGroup pcommandGroup4 = new ParallelCommandGroup();

    pcommandGroup4.addCommands(new SwerveSampleMoveCommand(s_drive, 0,0, -1, true, new Pose2d(), 0.05));

    ParallelCommandGroup pcommandGroup5 = new ParallelCommandGroup();

    pcommandGroup5.addCommands(new SwerveSampleMoveCommand(s_drive, 3,0, 0, true, new Pose2d(), 0.05));

    //Handoff
    ParallelCommandGroup pcommandGroup1 = new ParallelCommandGroup();

    pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    // pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    pcommandGroup1.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}));

    //shoot
    ParallelCommandGroup pcommandGroup2 = new ParallelCommandGroup();

    //pcommandGroup2.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    pcommandGroup2.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(2.0));
    pcommandGroup2.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(1.0));

    ParallelCommandGroup pcommandGroup6 = new ParallelCommandGroup();

    pcommandGroup6.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Stow));

    SequentialCommandGroup commandGroup = new SequentialCommandGroup();
    commandGroup.addCommands(pcommandGroup1.withTimeout(2));
    commandGroup.addCommands(pcommandGroup2.withTimeout(3));
    commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().stop();}).withTimeout(.2));
    commandGroup.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().stopShooterWheels();}).withTimeout(.2));
    commandGroup.addCommands(pcommandGroup6.withTimeout(1));
    commandGroup.addCommands(pcommandGroup0.withTimeout(10));
    // commandGroup.addCommands(pcommandGroup4.withTimeout(2));
    // commandGroup.addCommands(pcommandGroup5.withTimeout(2));

    return commandGroup;
  }

  public static Command leftAutonOneRingBlue() {
    SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
    s_drive.setMaxSpeeds(0.8, 0.5);

     //Move
    ParallelCommandGroup pcommandGroup0 = new ParallelCommandGroup();

    pcommandGroup0.addCommands(new SwerveSampleMoveCommand(s_drive, 2.5,0, 0.52, true, new Pose2d(), 0.05));

    //Handoff
    ParallelCommandGroup pcommandGroup1 = new ParallelCommandGroup();

    pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    // pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    pcommandGroup1.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}));

    //shoot
    ParallelCommandGroup pcommandGroup2 = new ParallelCommandGroup();

    //pcommandGroup2.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    pcommandGroup2.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(2.0));
    pcommandGroup2.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(1.0));

    SequentialCommandGroup commandGroup = new SequentialCommandGroup();
    commandGroup.addCommands(pcommandGroup0.withTimeout(5));
    commandGroup.addCommands(pcommandGroup1.withTimeout(3));
    commandGroup.addCommands(pcommandGroup2.withTimeout(5));
    commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().stop();}));
    commandGroup.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().stopShooterWheels();}));

    return commandGroup;
  }

  public static Command leftAutonTwoRing() {
    SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
    s_drive.setMaxSpeeds(0.8, 0.5);

    //Move
    ParallelCommandGroup pcommandGroup0 = new ParallelCommandGroup();

    pcommandGroup0.addCommands(new SwerveSampleMoveCommand(s_drive, 2.5,0, 0.52, true, new Pose2d(), 0.05));

    //hand-off
    ParallelCommandGroup pcommandGroup1 = new ParallelCommandGroup();

    pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    //pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    pcommandGroup1.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}));

    //shoot
    ParallelCommandGroup pcommandGroup2 = new ParallelCommandGroup();

    //pcommandGroup2.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    pcommandGroup2.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(2.0));
    pcommandGroup2.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(1.0));

    //pick-up
    ParallelCommandGroup pcommandGroup3 = new ParallelCommandGroup();

    pcommandGroup3.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PickupRing));
    pcommandGroup3.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1.0));


    SequentialCommandGroup commandGroup = new SequentialCommandGroup();
    //first ring
    // commandGroup.addCommands(pcommandGroup0.withTimeout(5));
    commandGroup.addCommands(pcommandGroup1.withTimeout(3));
    commandGroup.addCommands(pcommandGroup2.withTimeout(5));
    //second ring
    commandGroup.addCommands(pcommandGroup3.withTimeout(3));
    commandGroup.addCommands(pcommandGroup1.withTimeout(3));
    commandGroup.addCommands(pcommandGroup2.withTimeout(5));

    return commandGroup;
  }

  public static Command centerTwoRings() {
    SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
    s_drive.setMaxSpeeds(0.8, 0.5);

    //Move
    ParallelCommandGroup pcommandGroup0 = new ParallelCommandGroup();

    pcommandGroup0.addCommands(new SwerveSampleMoveCommand(s_drive, 1,0, 0, true, new Pose2d(), 0.05));

    //hand-off
    ParallelCommandGroup pcommandGroup1 = new ParallelCommandGroup();

    pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    //pcommandGroup1.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    pcommandGroup1.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}));

    //shoot
    ParallelCommandGroup pcommandGroup2 = new ParallelCommandGroup();

    //pcommandGroup2.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    pcommandGroup2.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
    pcommandGroup2.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(2.0));
    pcommandGroup2.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().releaseToShooter();}).withTimeout(1.0));

    //pick-up
    ParallelCommandGroup pcommandGroup3 = new ParallelCommandGroup();

    pcommandGroup3.addCommands(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PickupRing));
    pcommandGroup3.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1.0));


    SequentialCommandGroup commandGroup = new SequentialCommandGroup();
    //first ring
    commandGroup.addCommands(pcommandGroup0.withTimeout(5));
    commandGroup.addCommands(pcommandGroup1.withTimeout(3));
    commandGroup.addCommands(pcommandGroup2.withTimeout(5));
    //second ring
    commandGroup.addCommands(pcommandGroup3.withTimeout(3));
    commandGroup.addCommands(pcommandGroup1.withTimeout(3));
    commandGroup.addCommands(pcommandGroup2.withTimeout(5));

    return commandGroup;
  }

  public static Command sampleAutonCommand() {

    Command commandGroup = ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.TestRoutine);
    // SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    //commandGroup.addCommands(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance(), wristHandoffPosition));
    //commandGroup.addCommands(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance(), -270));
    //commandGroup.addCommands(new PositionSubsystemCommand(wristIntakePosition, WristSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(elbowIntakePosition, ElbowSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(elevatorIntakePosition, ElevatorSubsystem.getInstance()));
    //commandGroup.addCommands(new PositionSubsystemCommand(pivotShootPosition, PivotSubsystem.getInstance()));
    //commandGroup.addCommands(Commands.run(() -> {IntakeSubSystem.getInstance().doIntake(1.0);}).withTimeout(1.0));
    //commandGroup.addCommands(Commands.run(() -> {ShooterSubsystem.getInstance().startShooterWheels(1.0);}).withTimeout(1.0));



    // ParallelCommandGroup parallelGroup = new ParallelCommandGroup();
    // parallelGroup.addCommands(commandGroup);
    // parallelGroup.addCommands( 
    //   new WaitCommand(1).andThen(new InstantCommand(()->CommandInterruptor.getInstance().interruptSubsystem(WristSubsystem.getInstance().getName())))
    // );
    
    
    
    return commandGroup;
  }

  public static Command sampleWristCommand() {
    SequentialCommandGroup commandGroup = new SequentialCommandGroup();

    commandGroup.addCommands(new MoveWristCommand(970, true));
    commandGroup.addCommands(new WaitCommand(3));
    commandGroup.addCommands(new MoveWristCommand(966, false));
    return commandGroup;
  }
}