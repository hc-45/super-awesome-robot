/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import static org.wpilib.units.Units.*;

import com.stuypulse.robot.util.talonfx.TalonFXConfig;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.Distance;
import org.wpilib.units.measure.Mass;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public interface ElevatorConstants {
    interface ElevatorSettings {
        Angle STOWED_ANGLE = Rotations.zero();
        Angle SWITCH_ANGLE = Rotations.of(1.25);
        Angle SWITCH_TOLERANCE = Rotations.of(0.1);
        Angle SCALE_ANGLE = Rotations.of(3); // placeholder
        Angle SCALE_TOLERANCE = Rotations.of(0.1);

        Distance DRUM_RADIUS = Inches.of(0.7245);
        double GEAR_RATIO = 11.4/1;
        Mass MOVING_MASS = Pounds.of(32.0854568);

        Distance MIN_HEIGHT = Inches.of(54.75); // either this or 0
        Distance MAX_HEIGHT = Inches.of(94.6); // either this or 94.6 - 54.75
    }

    interface ElevatorPorts {
        int TR_MOTOR = 14;
        int BR_MOTOR = 15;
        int BL_MOTOR = 16;
        int TL_MOTOR = 17;
    }

    interface ElevatorConfigs {
        TalonFXConfig ELEVATOR_GEARBOX_MOTOR_CONFIG = new TalonFXConfig()
            .withInvertedValue(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            .withSensorToMechanismRatio(ElevatorSettings.GEAR_RATIO)
            .withStatorCurrentLimit(Amps.of(140.0))
            .withStatorCurrentLimitEnabled(false)
            .withSupplyCurrentLimit(Amps.of(100.0))
            .withSupplyCurrentLimitEnabled(true);
    }

    interface ElevatorGains {
    }
}
