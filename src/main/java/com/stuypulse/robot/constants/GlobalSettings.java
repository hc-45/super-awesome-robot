/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import static org.wpilib.units.Units.*;

import org.wpilib.framework.RobotBase;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.units.measure.Distance;
import org.wpilib.units.measure.LinearVelocity;
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

    interface DriveSettings {
        interface Alignment {
            interface Tolerances {
                Distance X_TOLERANCE = Inches.of(2.0);

                Distance Y_TOLERANCE = Inches.of(2.0);

                Rotation2d THETA_TOLERANCE = Rotation2d.fromDegrees(8);

                Pose2d POSE_TOLERANCE = new Pose2d(X_TOLERANCE.in(Meters), Y_TOLERANCE.in(Meters), THETA_TOLERANCE);

                LinearVelocity MAX_VELOCITY_WHEN_ALIGNED = MetersPerSecond.of(0.15);

                Time ALIGNMENT_DEBOUNCE = Seconds.of(0.15);
            }
        }
    }

    interface DriveGains {
        interface Alignment {
            double akP = 8.8;
            double akI = 0.0;
            double akD = 0.0;
        }
    }

    interface DriverConstants {
        double DEADBAND = 0.1;
        double ANGLE_KP = 5.0;
        double ANGLE_KI = 0.0;
        double ANGLE_KD = 0.4;
        double ANGLE_MAX_VELOCITY = 8.0;
        double ANGLE_MAX_ACCELERATION = 20.0;
        Time FF_START_DELAY = Seconds.of(2.0); // Secs
        double FF_RAMP_RATE = 0.1; // Volts/Sec
        double WHEEL_RADIUS_MAX_VELOCITY = 0.25; // Rad/Sec
        double WHEEL_RADIUS_RAMP_RATE = 0.05; // Rad/Sec^2
    }
}
