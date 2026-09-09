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
        interface Pivot {
            AngularVelocity MAX_VELOCITY = RPM.of(67); // placeholder
            AngularAcceleration MAX_ACCELERATION = RotationsPerSecondPerSecond.of(0.67); // placeholder

            Angle INTAKE_ANGLE = Degrees.of(180);
            Angle HELD_ANGLE = Degrees.of(0);
            Angle OUTTAKE_ANGLE = Degrees.of(180);

            // Sim stuff
            double GEAR_RATIO = 65.8 / 1;
            MomentOfInertia MOI = KilogramSquareMeters.of(0.2213322307);
            Distance ARM_LENGTH = Inches.of(16.3);
            Angle MIN_ANGLE = Degrees.of(0);
            Angle MAX_ANGLE = Degrees.of(180);
        }
        interface Rollers {
            Current ROLLER_SUPPLY_LIMIT = Amps.of(80.0); // placeholder
            
            double OUTTAKE_DUTY_CYCLE = -1.0;
            double INTAKE_DUTY_CYCLE = 1.0;
            double IDLE_DUTY_CYCLE = 0.0;

            // Sim stuff
            double GEAR_RATIO = 5 / 1;
            MomentOfInertia MOI = KilogramSquareMeters.of(0.0002609971); // placeholder
        }
    }

    interface ClawConfigs {
        TalonFXConfig CLAW_GRIPPER_MOTOR_CONFIG = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            .withMotionProfile(ClawSettings.Pivot.MAX_VELOCITY, ClawSettings.Pivot.MAX_ACCELERATION);
        TalonFXConfig CLAW_ROLLER_MOTOR_CONFIG = new TalonFXConfig()
            .withInvertedValue(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast)
            .withSupplyCurrentLimit(ClawSettings.Rollers.ROLLER_SUPPLY_LIMIT);
    }

    interface ClawPorts {
        int CLAW_PIVOT_MOTOR = 1;
        int CLAW_ROLLER_MOTOR = 2;
    }

    interface ClawGains {}
}
