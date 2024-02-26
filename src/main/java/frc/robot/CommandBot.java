// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.HoldSubsystemInPositionCommand;
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
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.Date;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
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

  public void init() {
    // Starts recording to data log
    DataLogManager.start();
    SwerveDriveSubsystem.getInstance().resetOdometry(new Pose2d()); //kp todo later to set initial pose
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
          () -> teleOpController.getXSpeedSwerve(), () -> teleOpController.getYSpeedSwerve(),
          () -> teleOpController.getRotation(), true, true));
     /*  else
        teleOpController.moveTrigger().whileTrue(s_drive.driveCommand(
          () -> -teleOpController.getXSpeedSwerve(), () -> -teleOpController.getYSpeedSwerve(),
          () -> -teleOpController.getRotation(), true, true));
        */
    }   
    IntakeSubSystem intake = IntakeSubSystem.getInstance();
    ShooterSubsystem shooter = ShooterSubsystem.getInstance();
    PivotSubsystem pivot = PivotSubsystem.getInstance();

    if (intake != null) {
      // Deploy the intake with the triangle button for the cone
      teleOpController.intakeTrigger().whileTrue(Commands.run(() -> {intake.doIntake(1.0);}));
      teleOpController.intakeTrigger().onFalse(Commands.runOnce(() -> {intake.stop();}));
      
      teleOpController.releaseToAMPTrigger().whileTrue(Commands.run(() -> {intake.releaseToAMP();}));
      teleOpController.releaseToAMPTrigger().onFalse(Commands.runOnce(() -> {intake.stop();}));
      
      if (shooter!=null) {
        teleOpController.getShootTrigger().whileTrue(Commands.run(() -> {shooter.startShooterWheels(1.0);}));
        teleOpController.getShootTrigger().onFalse(Commands.runOnce(() -> {intake.stop(); shooter.stopShooterWheels();}));
      }
    }

    if (pivot!=null) {
      teleOpController.getPivotTrigger().whileTrue(pivot.moveCommand(() -> teleOpController.getPivotSpeed()));
      teleOpController.getPivotTrigger().onFalse(Commands.runOnce(() -> {pivot.stop();}));
    }

    ElbowSubsystem elbow = ElbowSubsystem.getInstance();
    if (elbow != null) {
      if (dualController) {
        //elbow.setDefaultCommand(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
        //teleOpController.getElbowTrigger().whileFalse(Commands.runOnce(() -> {elbow.stop();}));// new HoldSubsystemInPositionCommand(elbow));
        teleOpController.getElbowTrigger().whileFalse(new HoldSubsystemInPositionCommand(elbow));
        teleOpController.getElbowTrigger().whileTrue(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
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
        teleOpController.getWristTrigger().whileFalse(new HoldSubsystemInPositionCommand(wrist));
        teleOpController.getWristTrigger().whileTrue(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
      }
      else {
        teleOpController.getWristTrigger().whileTrue(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
        teleOpController.getWristTrigger().onFalse(Commands.runOnce(() -> {wrist.stop();}));
      }
    }

    if (dualController) {
      teleOpController.holdElbowInPositionTrigger().whileTrue(new HoldSubsystemInPositionCommand(ElbowSubsystem.getInstance()));
      teleOpController.holdWristInPositionTrigger().whileTrue(new HoldSubsystemInPositionCommand(WristSubsystem.getInstance()));
    }

    ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
    if (elevator != null) {
        if (dualController)
        elevator.setDefaultCommand(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
      else {
        teleOpController.getElevatorTrigger().whileTrue(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
        teleOpController.getElevatorTrigger().onFalse(Commands.runOnce(() -> {elevator.stop();}));
      }
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
  }

  /**
   * Use this to define the command that runs during autonomous.
   *
   * <p>
   * Scheduled during {@link Robot#autonomousInit()}.
   */

  public Command getAutonomousCommand(Date autoStartTime) {
    return AutonController.getAutonCommand();
  }

  void periodic() {
    drive.periodic();
    GyroSubsystem.getInstance().periodic();
  }
}