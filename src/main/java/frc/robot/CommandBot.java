// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.HoldSubsystemInPositionCommand;
import frc.robot.commands.IntakeCommands;
import frc.robot.commands.MovePivotToPosition;
import frc.robot.commands.PositionSubsystemCommand;
import frc.robot.commands.ToggleShooterSpeedCommand;
import frc.robot.commands.arm_routines.ArmPresets;
import frc.robot.commands.arm_routines.logic.ArmRoutine;
import frc.robot.commands.arm_routines.logic.ArmRoutineCommandFactory;
import frc.robot.commands.autonCommands.HandoffAndShootCommand;
import frc.robot.commands.intake_commands.DetectRing;
import frc.robot.commands.intake_commands.IntakeRingCommand;
import frc.robot.controller.AutonController;
import frc.robot.controller.MyXboxController;
import frc.robot.controller.PS4Controller;
import frc.robot.controller.PS4ControllerSingle;
import frc.robot.controller.TeleOpController;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DifferentialDriveSubsystem;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.utils.GlobalState;
import frc.robot.vision.limelight.LimeLightFacade;
import frc.robot.vision.limelight.LimelightsContainer;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.Date;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**s
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class CommandBot {
  Subsystem drive;
  LimeLightFacade m_limelight;

  public void init() {
    SwerveDriveSubsystem.getInstance().resetOdometry(new Pose2d()); //kp todo later to set initial pose
    m_limelight = new LimeLightFacade();
    LimelightsContainer.getInstance().addLimeLight("limelight", m_limelight);

    NamedCommands.registerCommand("pickupWithSensor", new IntakeRingCommand(true));
    NamedCommands.registerCommand("handoffAndShoot", IntakeCommands.handoffAndShootCommand());
    NamedCommands.registerCommand("moveToPickup", IntakeCommands.moveToIntakePos());
    NamedCommands.registerCommand("detectRing", new DetectRing());
  }

  /**
   ****** Use this method to define bindings between conditions and commands. These are
   * useful for
   * automating robot behaviors based on button and sensor input.
   *
   * <p>
   * Should be called during {@link Robot#robotInit()}.
   *
   * <p>
   * Event binding methods are available on the {@link Trigger} class.
   */
  public void configureBindings() {
    boolean dualController;
    TeleOpController teleOpController;
    
    if (OIConstants.controllerType.equals("PS4")) {
      teleOpController = PS4Controller.getInstance();
      dualController = true;
    } else if (OIConstants.controllerType.equals("PS4S")) {
       teleOpController = PS4ControllerSingle.getInstance();
       dualController = false;
    } else {
      teleOpController = MyXboxController.getInstance();
      dualController = true;
    }

    //used to override and cancel active commands manually
    teleOpController.cancelAllCommandsTrigger().whileTrue(Commands.run(()->{CommandScheduler.getInstance().cancelAll();}));

    System.out.println("Configring Bindings with driveType:" + DriveConstants.driveType);
    if (DriveConstants.driveType.startsWith("DIFF")) {
      DifferentialDriveSubsystem d_drive = DifferentialDriveSubsystem.getInstance();
      drive = d_drive;
      // Note: Pass lamdba fn to get speed/rot and not the current speed/rot
      d_drive.setDefaultCommand(
          d_drive.driveCommand(() -> -teleOpController.getYSpeedSwerve(), () -> -teleOpController.getRotation()));
    } else {
      SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
      drive = s_drive;
      // Control the swerve drive with split-stick controls (Field coordinates are y is horizontal and x is +ve towards alliance from center)
      //hence you see x and y reversed when passing to drive
      if (dualController)
        s_drive.setDefaultCommand(s_drive.driveCommand(
          () -> teleOpController.getYSpeedSwerve(), () -> teleOpController.getXSpeedSwerve(),
          () -> teleOpController.getRotation(), true, false));
     /*  else
        teleOpController.moveTrigger().whileTrue(s_drive.driveCommand(
          () -> -teleOpController.getXSpeedSwerve(), () -> -teleOpController.getYSpeedSwerve(),
          () -> -teleOpController.getRotation(), true, true));
        */

        teleOpController.getResetTrigger().whileTrue(Commands.run(() -> {
          s_drive.zeroGyro();
          System.out.println("Gyro reset button pressed value = "+GyroSubsystem.getInstance().getYaw());
          SwerveDriveSubsystem.getInstance().resetOdometry(new Pose2d()); //kp todo later to set initial pose
        }));
        
    }   
    IntakeSubSystem intake = IntakeSubSystem.getInstance();
    ShooterSubsystem shooter = ShooterSubsystem.getInstance();
    PivotSubsystem pivot = PivotSubsystem.getInstance();

    if (intake != null) {
      // Deploy the intake with the triangle button for the cone
      teleOpController.intakeTrigger().onTrue(new IntakeRingCommand(true));
      teleOpController.intakeTriggerDrive().whileTrue(Commands.run(() -> {intake.doIntake(1.0);}));
      teleOpController.intakeTriggerDrive().onFalse(Commands.runOnce(() -> {intake.stop();}));
      
      teleOpController.releaseToAMPTrigger().whileTrue(Commands.run(() -> {intake.releaseToAMP();}));
      teleOpController.releaseToAMPTrigger().onFalse(Commands.runOnce(() -> {intake.stop();}));
      
      if (shooter!=null) {
        teleOpController.getShootTrigger().onTrue(new ToggleShooterSpeedCommand());
      }
    }

    if (pivot!=null) {
      teleOpController.getPivotTriggerDown().whileTrue(Commands.run(() -> {pivot.move(.6);}));
      teleOpController.getPivotTriggerUp().whileTrue(Commands.run(() -> {pivot.move(-.6);}));
      teleOpController.getPivotTriggerDown().onFalse(Commands.runOnce(() -> {pivot.stop();}));
      teleOpController.getPivotTriggerUp().onFalse(Commands.runOnce(() -> {pivot.stop();}));
      teleOpController.getPivotPresetTrigger().onTrue(new MovePivotToPosition(30));
      // teleOpController.getPivotTriggerUp().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.createPivotPreset(100)));

    }

    ElbowSubsystem elbow = ElbowSubsystem.getInstance();
    if (elbow != null) {
      if (dualController) {
        elbow.setDefaultCommand(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
        teleOpController.getElbowTrigger().whileFalse(Commands.runOnce(() -> {elbow.stop();}));// new HoldSubsystemInPositionCommand(elbow));
        // teleOpController.getElbowTrigger().whileFalse(new HoldSubsystemInPositionCommand(elbow));
        // teleOpController.getElbowTrigger().whileTrue(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
      }
      else {
        teleOpController.getElbowTrigger().onTrue(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
        teleOpController.getElbowTrigger().onFalse(Commands.runOnce(() -> {elbow.stop();}));
      }
    }

    WristSubsystem wrist = WristSubsystem.getInstance();
    if (wrist != null) {
      if (dualController) {
        //wrist.setDefaultCommand(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
        // teleOpController.getWristTrigger().whileFalse(new HoldSubsystemInPositionCommand(wrist));
        teleOpController.getWristTrigger().whileTrue(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
        teleOpController.getWristTrigger().onFalse(Commands.runOnce(() -> {wrist.stop();}));
        // teleOpController.moveWristTrigger().whileTrue(new PositionSubsystemCommand(80, wrist));
      }
      else {
        teleOpController.getWristTrigger().whileTrue(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
        teleOpController.getWristTrigger().onFalse(Commands.runOnce(() -> {wrist.stop();}));
      }
    }

    /*if (dualController) {
      teleOpController.holdElbowInPositionTrigger().whileTrue(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
      teleOpController.holdWristInPositionTrigger().whileTrue(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    }*/


    ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
    if (elevator != null) {
        teleOpController.getElevatorTrigger().whileTrue(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
        teleOpController.getElevatorTrigger().onFalse(Commands.runOnce(() -> {elevator.stop();}));
        /*if (dualController)
        elevator.setDefaultCommand(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
      else {
        teleOpController.getElevatorTrigger().whileTrue(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
        teleOpController.getElevatorTrigger().onFalse(Commands.runOnce(() -> {elevator.stop();}));
      }*/

    }

    ClimbSubsystem hook = ClimbSubsystem.getInstance();
    if (hook!=null) {
      if (dualController) {
      teleOpController.getHookUpTrigger().whileTrue(hook.moveCommand(() -> teleOpController.getHookUpSpeed()));
      teleOpController.getHookDownTrigger().whileTrue(hook.moveCommand(() -> teleOpController.getHookDownSpeed()));
      teleOpController.getHookUpTrigger().onFalse(Commands.runOnce(() -> {hook.stop();}));
      teleOpController.getHookDownTrigger().onFalse(Commands.runOnce(() -> {hook.stop();}));
      }
    }

    //preset triggers
    // teleOpController.resetLastKnownPresetNameTrigger().onTrue(Commands.runOnce(()->{GlobalState.getInstance().setPreviousPresetRun("");}));
    teleOpController.pickupPresetTrigger().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PickupRing)); //cross
    teleOpController.stowPresetTrigger().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Stow)); //square
    teleOpController.ampPresetTrigger().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.AmpDropOff)); //triangle
    teleOpController.handoffPresetTrigger().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.Handoff)); //circle

    // teleOpController.getPivotTriggerDown().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotDropTilt));
    // teleOpController.getPivotTriggerUp().onTrue(ArmRoutineCommandFactory.getInstance().executeArmRoutine(ArmPresets.PivotShootTilt));
    

    //speed control toggle between 1.0 or 0.4, see GlobalState for speedsb
    teleOpController.slowMaxSpeedTrigger().onTrue(Commands.runOnce(() -> {
          GlobalState.getInstance().toggleMaxSpeed();
          Double maxspeed = GlobalState.getInstance().getMaxSpeed();
          System.out.println("Max speed set to "+maxspeed);
          SwerveDriveSubsystem.getInstance().setMaxSpeeds(maxspeed, maxspeed);
      }));
    
  }

  /**
   * Use this to define the command that runs during autonomous.
   *
   * <p>
   * Scheduled during {@link Robot#autonomousInit()}.
   */

  public Command getAutonomousCommand(Date autoStartTime) {
    // return AutonController.getAutonCommand();
    return new PathPlannerAuto("New Auto");
  }

  void periodic() {
    drive.periodic();
    GyroSubsystem.getInstance().periodic();
    m_limelight.updateDashboard();
  }
}