/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import static org.wpilib.units.Units.*;

import org.wpilib.framework.RobotBase;
import org.wpilib.units.measure.Time;

import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;

/**
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use DogLog's tunables in order to have tunable values that we can edit
 * from external dashboards.
 */
public interface GlobalSettings {
    Time DT = Milliseconds.of(20);

    interface EnabledSubsystems {
        LoggedNetworkBoolean SWERVE = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Swerve", true);
        LoggedNetworkBoolean BUDDY_CLIMB = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Buddy Climb", true);
        LoggedNetworkBoolean ELEVATOR = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Elevator", true);
        LoggedNetworkBoolean CLAW = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Claw", true);
        LoggedNetworkBoolean VISION = new LoggedNetworkBoolean("/Tuning/Enabled Subsystems/Vision", true);
    }

    // AKit stuff
    RobotMode SIM_MODE = RobotMode.SIM;
    RobotMode CURRENT_MODE = RobotBase.isReal() ? RobotMode.REAL : SIM_MODE;

    enum RobotMode {
        /** Running on a real robot. */
        REAL,

        /** Running in simulation. */
        SIM,

        /** Replaying from a log file. */
        REPLAY
    }

    VisionMode VISION_MODE = VisionMode.LIMELIGHT;

    enum VisionMode {
        LIMELIGHT,
        PHOTON
    }
}
