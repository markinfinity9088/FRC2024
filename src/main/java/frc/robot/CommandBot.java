// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.controller.AutonController;
import frc.robot.controller.MyXboxController;
import frc.robot.controller.PS4Controller;
import frc.robot.controller.TeleOpController;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DifferentialDriveSubsystem;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.IntakeSubSystem;
import frc.robot.subsystems.PulleySubsystem;

import java.util.Date;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
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
    TeleOpController teleOpController = OIConstants.controllerType.equals("PS4")
      ? PS4Controller.getInstance()
      : MyXboxController.getInstance();

    System.out.println("Configring Bindings with driveType:" + DriveConstants.driveType);
    if (DriveConstants.driveType.startsWith("DIFF")) {
      DifferentialDriveSubsystem d_drive = DifferentialDriveSubsystem.getInstance();
      drive = d_drive;
      // Note: Pass lamdba fn to get speed/rot and not the current speed/rot
      d_drive.setDefaultCommand(
          d_drive.driveCommand(() -> -teleOpController.getYSpeed(), () -> -teleOpController.getRotation()));
    } else {
      SwerveDriveSubsystem s_drive = SwerveDriveSubsystem.getInstance();
      drive = s_drive;
      // Control the swerve drive with split-stick controls (Field coordinates are y is horizontal and x is +ve towards alliance from center)
      //hence you see x and y reversed when passing to drive
      s_drive.setDefaultCommand(s_drive.driveCommand(
          () -> -MathUtil.applyDeadband(teleOpController.getYSpeed(), OIConstants.kDriveDeadband),
          () -> -MathUtil.applyDeadband(teleOpController.getXSpeed(), OIConstants.kDriveDeadband),
          () -> -MathUtil.applyDeadband(teleOpController.getRotation(), OIConstants.kDriveDeadband), 
          true, true));
      /*
       * teleOpController.moveTrigger().whileTrue(s_drive.driveCommand(() ->
       * -teleOpController.getXSpeed(),
       * () -> -teleOpController.getYSpeed(),
       * () -> -teleOpController.getRotation(), true, true));
       */
    }   
    IntakeSubSystem m_intake = IntakeSubSystem.getInstance();
    if (m_intake != null) {
      // Deploy the intake with the triangle button for the cone
      teleOpController.intakeTrigger().whileTrue(Commands.run(() -> {m_intake.doIntake();}));
      teleOpController.intakeTrigger().onFalse(Commands.run(() -> {m_intake.stop();}));
      // Release the intake with the cross button for the cube
      teleOpController.releaseToAMPTrigger().whileTrue(Commands.run(() -> {m_intake.releaseToAMP();}));
      teleOpController.releaseToAMPTrigger().onFalse(Commands.run(() -> {m_intake.stop();}));
      // Deploy the intake with the square button for the cube
      teleOpController.releaseToShooterTrigger().whileTrue(Commands.run(() -> {m_intake.releaseToShooter();}));
      teleOpController.releaseToShooterTrigger().onFalse(Commands.run(() -> {m_intake.stop();}));
    }

    ElbowSubsystem elbow = ElbowSubsystem.getInstance();
    if (elbow != null) {
      //elbow.setDefaultCommand(Commands.run(() -> {elbow.stop();}));
      teleOpController.getElbowTrigger().whileTrue(elbow.moveCommand(() -> teleOpController.getElbowSpeed()));
    }

    WristSubsystem wrist = WristSubsystem.getInstance();
    if (wrist != null) {
      //wrist.setDefaultCommand(Commands.run(() -> {wrist.stop();}));
      teleOpController.getWristTrigger().whileTrue(wrist.moveCommand(() -> teleOpController.getWristSpeed()));
    }

    ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
    if (elevator != null) {
      //elevator.setDefaultCommand(Commands.run(() -> {elevator.stop();}));
      teleOpController.getElevatorTrigger().whileTrue(elevator.moveCommand(() -> teleOpController.getElevatorSpeed()));
    }

    ClimbSubsystem hook = ClimbSubsystem.getInstance();
    if (hook!=null) {
      // Lifting the robot up on to chain
      teleOpController.raiseHookTrigger().whileTrue(hook.raiseCommand(() -> teleOpController.getHookRaiseSpeed()));
      // Lowering the hook
      teleOpController.lowerHookTrigger().whileTrue(hook.lowerCommand(() -> -teleOpController.getHookRaiseSpeed()));
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