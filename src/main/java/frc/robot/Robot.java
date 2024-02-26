// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Date;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ElbowSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.utils.RuntimeConfig;

import org.littletonrobotics.junction.LoggedRobot;

import com.revrobotics.REVPhysicsSim;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {
  private Command m_autonomousCommand;

  private final CommandBot m_robot = new CommandBot();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    m_robot.init();
    SmartDashboard.putBoolean("Reset", false);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    boolean refresh = SmartDashboard.getBoolean("Reset", false);
    if (refresh) {
      System.out.println("Resetting subsystems..");
      WristSubsystem.getInstance().reset();
      ElbowSubsystem.getInstance().reset();
      ElevatorSubsystem.getInstance().reset();
      PivotSubsystem.getInstance().reset();
      ClimbSubsystem.getInstance().reset();
      SmartDashboard.putBoolean("Reset", false); 
    }
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    // Configure default commands and condition bindings on robot startup
    GyroSubsystem.getInstance().init();

    m_autonomousCommand = m_robot.getAutonomousCommand(new Date());

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    WristSubsystem.getInstance().periodic();
    ElbowSubsystem.getInstance().periodic();
    ElevatorSubsystem.getInstance().periodic();
    PivotSubsystem.getInstance().periodic();
    ClimbSubsystem.getInstance().periodic();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    // Configure default commands and condition bindings on robot startup
    m_robot.configureBindings();
    GyroSubsystem.getInstance().init();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    m_robot.periodic();
    WristSubsystem.getInstance().periodic();
    ElbowSubsystem.getInstance().periodic();
    ElevatorSubsystem.getInstance().periodic();
    PivotSubsystem.getInstance().periodic();
    ClimbSubsystem.getInstance().periodic();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    System.out.println("Test Init");
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
    System.out.println("Simulation Mode Init");
    RuntimeConfig.is_simulator_mode = true;
  }

  @Override
  public void simulationPeriodic(){
    REVPhysicsSim.getInstance().run();
    WristSubsystem.getInstance().simulationPeriodic();
    ElbowSubsystem.getInstance().simulationPeriodic();
    ElevatorSubsystem.getInstance().simulationPeriodic();
    PivotSubsystem.getInstance().simulationPeriodic();
    ClimbSubsystem.getInstance().simulationPeriodic();
  }

}
