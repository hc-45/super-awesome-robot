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
        public Current pivotMotorSupplyCurrent = Amps.zero();
        public Current pivotMotorStatorCurrent = Amps.zero();
        public Angle pivotMotorPosition = Rotations.zero();
        public boolean pivotMotorMotionMagicAtTarget = false;
        public Voltage pivotMotorVoltage = Volts.zero();

        public Current rollerMotorSupplyCurrent = Amps.zero();
        public Current rollerMotorStatorCurrent = Amps.zero();
        public Temperature rollerMotorTemperature = Celsius.zero();
        public AngularVelocity rollerMotorAngularVelocity = RotationsPerSecond.zero();
        public Voltage rollerMotorVoltage = Volts.zero();
    }

    enum PivotOutputMode {
        IDLE,
        MOTION_MAGIC;
    }

    enum RollerOutputMode {
        IDLE,
        DUTY_CYCLE;
    }

    class PivotOutputs {
        @AutoLogOutput(key = "Claw/Pivot/Output Mode")
        public PivotOutputMode pivotOutputMode = PivotOutputMode.IDLE;
        @AutoLogOutput(key = "Claw/Pivot/Profile Setpoint")
        public Angle pivotProfileSetpoint = Rotations.zero();
    }

    class RollerOutputs {
        @AutoLogOutput(key = "Claw/Rollers/Output Mode")
        public RollerOutputMode rollerOutputMode = RollerOutputMode.IDLE;
        @AutoLogOutput(key = "Claw/Rollers/Target Duty Cycle")
        public double rollerTargetDutyCycle = 0.0;
    }

    default StatusCode updateInputs(final ClawInputs inputs) {
        return StatusCode.OK;
    }

    default StatusCode applyPivotOutputs(final PivotOutputs outputs) {
        return StatusCode.OK;
    }

    default StatusCode applyRollerOutputs(final RollerOutputs outputs) {
        return StatusCode.OK;
    }
}
