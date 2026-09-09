/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import static org.wpilib.units.Units.*;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.StatusCode;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface ElevatorIO {
    @AutoLog
    class ElevatorInputs {
        // top right (leader) motor
        public Current TRMotorSupplyCurrent = Amps.zero();
        public Current TRMotorStatorCurrent = Amps.zero();
        public Angle TRMotorPosition = Rotations.zero();
        public boolean TRMotorMotionMagicAtTarget = false;
        public Voltage TRMotorVoltage = Volts.zero();
    }

    enum ElevatorOutputMode {
        IDLE,
        MOTION_MAGIC
    }

    class ElevatorOutputs {
        @AutoLogOutput(key = "Elevator/Output Mode")
        ElevatorOutputMode outputMode = ElevatorOutputMode.IDLE;
        @AutoLogOutput(key = "Elevator/Profile Setpoint")
        Angle profileSetpoint = Rotations.zero();
    }

    default StatusCode updateInputs(final ElevatorInputs inputs) {
        return StatusCode.OK;
    }

    default StatusCode applyOutputs(final ElevatorOutputs outputs) {
        return StatusCode.OK;
    }
}
