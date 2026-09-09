/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot;

import com.stuypulse.robot.commands.auton.AutonomousRoutines;
import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.claw.ClawIO;
import com.stuypulse.robot.subsystems.claw.ClawIOSim;
import com.stuypulse.robot.subsystems.claw.ClawIOTalonFX;

import org.wpilib.command3.Command;
import org.wpilib.command3.button.CommandNiDsXboxController;
import org.wpilib.smartdashboard.SendableChooser;
import org.wpilib.smartdashboard.SmartDashboard;

import dev.doglog.DogLog;
import dev.doglog.DogLogOptions;

public class RobotContainer {

    // Gamepads
    public final CommandNiDsXboxController driver =
            new CommandNiDsXboxController(GlobalPorts.Gamepad.DRIVER);
    public final CommandNiDsXboxController operator =
            new CommandNiDsXboxController(GlobalPorts.Gamepad.OPERATOR);

    // Subsystem
    private final Claw claw;

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        switch (GlobalSettings.currentMode) {
            case REAL -> {
                claw = new Claw(new ClawIOTalonFX());
            }
            case SIM -> {
                claw = new Claw(new ClawIOSim());
            }
            default -> {
                claw = new Claw(new ClawIO() {});
            }
        }
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

    private void configureDefaultCommands() {}

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {}

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
