/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.buddyclimb;

import static org.wpilib.units.Units.*;

import com.stuypulse.robot.util.talonfx.TalonFXConfig;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.signals.NeutralModeValue;

public interface BuddyClimbConstants {
    interface BuddyClimbSettings {
        Angle HELD_ANGLE = Degrees.zero();
        Angle DEPLOYED_ANGLE = Rotations.of(3); // placeholder
    }

    interface BuddyClimbPorts {
        int WINCH_MOTOR = 20;
    }

    interface BuddyClimbConfigs {
        TalonFXConfig WINCH_MOTOR_CONFIG = new TalonFXConfig()
            .withNeutralMode(NeutralModeValue.Brake);
    }

    interface BuddyClimbGains {}
}
