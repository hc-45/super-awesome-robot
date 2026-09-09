/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot;

import com.stuypulse.robot.commands.auton.AutonomousRoutines;
import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.constants.GlobalSettings.VisionMode;
import com.stuypulse.robot.generated.TunerConstants;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyClimb;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyIO;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyIOSim;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyIOTalonFX;
import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.claw.ClawIO;
import com.stuypulse.robot.subsystems.claw.ClawIOSim;
import com.stuypulse.robot.subsystems.claw.ClawIOTalonFX;
import com.stuypulse.robot.subsystems.elevator.Elevator;
import com.stuypulse.robot.subsystems.elevator.ElevatorIO;
import com.stuypulse.robot.subsystems.elevator.ElevatorIOSim;
import com.stuypulse.robot.subsystems.elevator.ElevatorIOTalonFX;
import com.stuypulse.robot.subsystems.swerve.Drive;
import com.stuypulse.robot.subsystems.swerve.GyroIO;
import com.stuypulse.robot.subsystems.swerve.GyroIOPigeon2;
import com.stuypulse.robot.subsystems.swerve.ModuleIO;
import com.stuypulse.robot.subsystems.swerve.ModuleIOSim;
import com.stuypulse.robot.subsystems.swerve.ModuleIOTalonFX;
import com.stuypulse.robot.subsystems.vision.Vision;
import com.stuypulse.robot.subsystems.vision.VisionConstants.Cameras;
import com.stuypulse.robot.subsystems.vision.VisionIO;
import com.stuypulse.robot.subsystems.vision.VisionIOLimelight;
import com.stuypulse.robot.subsystems.vision.VisionIOPhotonVision;
import com.stuypulse.robot.subsystems.vision.VisionIOPhotonVisionSim;

import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandNiDsXboxController;
import org.wpilib.smartdashboard.SendableChooser;
import org.wpilib.smartdashboard.SmartDashboard;

import dev.doglog.DogLog;
import dev.doglog.DogLogOptions;
import java.util.EnumMap;

public class RobotContainer {

    // Gamepads
    public final CommandNiDsXboxController driver = new CommandNiDsXboxController(GlobalPorts.Gamepad.DRIVER);
    public final CommandNiDsXboxController operator = new CommandNiDsXboxController(GlobalPorts.Gamepad.OPERATOR);

    // Subsystem
    private final Drive drive;
    private final BuddyClimb buddyClimb;
    private final Claw claw;
    private final Elevator elevator;
    private final Vision vision;

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        switch (GlobalSettings.CURRENT_MODE) {
            case REAL -> {
                drive = new Drive(
                        new GyroIOPigeon2(),
                        new ModuleIOTalonFX(TunerConstants.FrontLeft),
                        new ModuleIOTalonFX(TunerConstants.FrontRight),
                        new ModuleIOTalonFX(TunerConstants.BackLeft),
                        new ModuleIOTalonFX(TunerConstants.BackRight));
                buddyClimb = new BuddyClimb(new BuddyIOTalonFX());
                claw = new Claw(new ClawIOTalonFX());
                elevator = new Elevator(new ElevatorIOTalonFX());
            }
            case SIM -> {
                drive = new Drive(
                        new GyroIO() {
                        },
                        new ModuleIOSim(TunerConstants.FrontLeft),
                        new ModuleIOSim(TunerConstants.FrontRight),
                        new ModuleIOSim(TunerConstants.BackLeft),
                        new ModuleIOSim(TunerConstants.BackRight));
                buddyClimb = new BuddyClimb(new BuddyIOSim());
                claw = new Claw(new ClawIOSim());
                elevator = new Elevator(new ElevatorIOSim());
            }
            default -> {
                drive = new Drive(
                        new GyroIO() {
                        },
                        new ModuleIO() {
                        },
                        new ModuleIO() {
                        },
                        new ModuleIO() {
                        },
                        new ModuleIO() {
                        });
                buddyClimb = new BuddyClimb(new BuddyIO() {
                });
                claw = new Claw(new ClawIO() {
                });
                elevator = new Elevator(new ElevatorIO() {
                });
            }
        }
        final EnumMap<Cameras, VisionIO> cameraIOMap = new EnumMap<>(Cameras.class);

        switch (GlobalSettings.CURRENT_MODE) {
            case REAL -> {
                for (Cameras camera : Cameras.values()) {
                    if (GlobalSettings.VISION_MODE == VisionMode.LIMELIGHT) {
                        cameraIOMap.put(camera, new VisionIOLimelight(camera.getName(), drive::getRotation));
                    } else {
                        cameraIOMap.put(
                                camera, new VisionIOPhotonVision(camera.getName(), camera.getRobotToCamera()));
                    }
                }
            }

            case SIM -> {
                for (Cameras camera : Cameras.values()) {
                    cameraIOMap.put(
                            camera,
                            new VisionIOPhotonVisionSim(
                                    camera.getName(), camera.getRobotToCamera(), drive::getPose));
                }
            }

            // For replay mode
            default -> {
                for (Cameras camera : Cameras.values()) {
                    cameraIOMap.put(camera, new VisionIO() {
                    });
                }
            }
        }

        vision = new Vision(drive::addVisionMeasurement, cameraIOMap);

        configureLogging();
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();
    }

    /***************/
    /*** LOGGING ***/
    /***************/

    private void configureLogging() {
        DogLog.setOptions(
                new DogLogOptions().withCaptureDs(true).withNtTunables(true).withLogExtras(true));
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
    }

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", AutonomousRoutines.doNothingAuton());

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return The command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}
