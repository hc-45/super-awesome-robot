/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import static org.wpilib.units.Units.*;
import org.wpilib.units.measure.*;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.stuypulse.robot.util.talonfx.TalonFXConfig;

public interface ClawConstants {
    interface ClawSettings {
        interface Gripper {
            AngularVelocity MAX_GRIPPER_VELOCITY = RPM.of(67); // placeholder
            AngularAcceleration MAX_GRIPPER_ACCELERATION = RotationsPerSecondPerSecond.of(0.67); // placeholder

            Current GRIPPER_SUPPLY_LIMIT = Amps.of(80.0);
            Time GRIPPER_RAMP_RATE = Milliseconds.of(250); // is this even applicable?!?!

            Voltage SQUEEZE_VOLTAGE = Volts.of(6);
            Voltage IDLE_VOLTAGE = Volts.zero();

            Angle OPEN_ANGLE = Degrees.of(67); // placeholder
            Angle GRAB_ANGLE = Degrees.of(30); // placeholder
        }
        interface Rollers {
            Current ROLLER_SUPPLY_LIMIT = Amps.of(80.0) // placeholder
            
            double INTAKE_DUTY_CYCLE = 0.75; // placeholder
            double IDLE_DUTY_CYCLE = 0.0;
        }
        interface Pivot {}
    }

    interface ClawConfigs {
        TalonFXConfig CLAW_GRIPPER_MOTOR_CONFIG = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            .withMotionProfile(ClawSettings.Gripper.MAX_GRIPPER_VELOCITY, ClawSettings.Gripper.MAX_GRIPPER_ACCELERATION)
            .withSupplyCurrentLimit(ClawSettings.Gripper.GRIPPER_SUPPLY_LIMIT)
            .withRampRate(ClawSettings.Gripper.GRIPPER_RAMP_RATE);
    }

    interface ClawPorts {
        int CLAW_GRIPPER_MOTOR = 1;
    }

    interface ClawGains {}
}
