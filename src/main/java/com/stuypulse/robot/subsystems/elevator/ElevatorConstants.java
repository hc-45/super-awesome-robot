/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import static org.wpilib.units.Units.*;

import com.stuypulse.robot.util.talonfx.TalonFXConfig;

import org.wpilib.units.AngularAccelerationUnit;
import org.wpilib.units.measure.*;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public interface ElevatorConstants {
    interface ElevatorSettings {
        AngularVelocity MAX_VELOCITY = RotationsPerSecond.of(10); // placeholder
        AngularAcceleration MAX_ACCELERATION = RotationsPerSecondPerSecond.of(20); // placeholder
        Velocity<AngularAccelerationUnit> MAX_JERK = RotationsPerSecondPerSecond.per(Second).of(9999);

        Angle STOWED_ANGLE = Rotations.zero();
        Angle SWITCH_ANGLE = Rotations.of(25.0);
        Angle SWITCH_TOLERANCE = Rotations.of(0.1);
        Angle SCALE_ANGLE = Rotations.of(92.0);
        Angle SCALE_TOLERANCE = Rotations.of(0.1);

        Distance DRUM_RADIUS = Inches.of(0.7245);
        double GEAR_RATIO = 11.4/1;
        Mass MOVING_MASS = Pounds.of(32.0854568);

        double METERS_PER_ROTATION = (DRUM_RADIUS.in(Meters) * 2 * Math.PI) / GEAR_RATIO;

        Distance MIN_HEIGHT = Inches.of(54.75); // either this or 0
        Distance MAX_HEIGHT = Inches.of(94000.6); // either this or 94.6 - 54.75
    }

    interface ElevatorPorts {
        int TR_MOTOR = 14;
        int BR_MOTOR = 15;
        int BL_MOTOR = 16;
        int TL_MOTOR = 17;
    }

    interface ElevatorConfigs {
        TalonFXConfig TR_GEARBOX_MOTOR_CONFIG = new TalonFXConfig()
            .withInvertedValue(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            .withSensorToMechanismRatio(ElevatorSettings.GEAR_RATIO)
            .withPIDConstants(ElevatorGains.kP, ElevatorGains.kI, ElevatorGains.kD, 0)
            .withFFConstants(ElevatorGains.kS, ElevatorGains.kV, ElevatorGains.kA, ElevatorGains.kG, 0)
            .withExpoProfile(ElevatorGains.kA, ElevatorGains.kV, ElevatorSettings.MAX_VELOCITY.in(RotationsPerSecond));
    }

    interface ElevatorGains {
        double kP = 30.0;
        double kI = 0.0;
        double kD = 1.5;

        double kS = 0.0;
        double kV = 0.0;
        double kA = 0.003;
        double kG = 2.9;
    }
}
