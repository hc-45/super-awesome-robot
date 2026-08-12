/************************ PROJECT PHIL ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import static org.wpilib.units.Units.*;

import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;
import org.wpilib.framework.RobotBase;
import org.wpilib.units.measure.Time;

/**
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use DogLog's tunables in order to have tunable values that we can edit
 * from external dashboards.
 */
public interface Settings {
    public static final Time DT = Milliseconds.of(20);

    public interface EnabledSubsystems {
        LoggedNetworkBoolean SWERVE = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Swerve", true);
        LoggedNetworkBoolean CLIMB = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Climb", true);
        LoggedNetworkBoolean ELEVATOR = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Elevator", true);
    }

    // A Kit stuff

    public static final Mode simMode = Mode.SIM;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

    public static enum Mode {
        /** Running on a real robot. */
        REAL,

        /** Running a physics simulator. */
        SIM,

        /** Replaying from a log file. */
        REPLAY
    }
}
