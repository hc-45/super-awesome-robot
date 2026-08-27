/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import static org.wpilib.units.Units.*;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.StatusCode;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface ClawIO {
    @AutoLog
    class ClawInputs {
        public Current rollerMotorSupplyCurrent = Amps.zero();
        public Current rollerMotorStatorCurrent = Amps.zero();
        public Temperature rollerMotorTemperature = Celsius.zero();
        public AngularVelocity rollerMotorVelocity = RotationsPerSecond.zero();
        public Voltage rollerMotorAppliedVoltage = Volts.zero();

        public Current pivotMotorSupplyCurrent = Amps.zero();
        public Current pivotMotorStatorCurrent = Amps.zero();
        public Angle pivotMotorPosition = Rotations.zero();
        public boolean pivotMotorMotionMagicAtTarget = false;
        public Voltage pivotMotorVoltage = Volts.zero();
    }

    enum GripperOutputMode {
        IDLE,
        VOLTAGE,
        MOTION_MAGIC;
    }

    class ClawOutputs {
        @AutoLogOutput(key = "Claw/Gripper/Output Mode")
        public GripperOutputMode gripperOutputMode = GripperOutputMode.VOLTAGE;
        @AutoLogOutput(key = "Claw/Gripper/Target Voltage")
        public Voltage gripperTargetVoltage = Volts.zero();
        @AutoLogOutput(key = "Claw/Gripper/Profile Setpoint")
        public Angle gripperProfileSetpoint = Rotations.zero();
    }

    default StatusCode updateInputs(ClawInputs inputs) {
        return StatusCode.OK;
    }

    default StatusCode applyOutputs(ClawOutputs outputs) {
        return StatusCode.OK;
    }
}
