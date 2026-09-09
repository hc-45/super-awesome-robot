/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.buddyclimb;

import static org.wpilib.units.Units.*;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.StatusCode;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface BuddyIO {
    @AutoLog
    class BuddyClimbInputs {
        public Current winchMotorSupplyCurrent = Amps.zero();
        public Current winchMotorStatorCurrent = Amps.zero();
        public Angle winchMotorPosition = Rotations.zero();
        public Voltage winchMotorVoltage = Volts.zero();
    }

    enum BuddyClimbOutputMode {
        STOP,
        POSITION_VOLTAGE
    }

    class BuddyClimbOutputs {
        @AutoLogOutput(key = "Buddy Climb/State")
        public BuddyClimbOutputMode outputMode = BuddyClimbOutputMode.STOP;
        @AutoLogOutput(key = "Buddy Climb/Target Position")
        public Angle targetPosition = Rotations.zero();
    }


    default StatusCode updateInputs(final BuddyClimbInputs inputs) {
        return StatusCode.OK;
    }

    default StatusCode applyOutputs(final BuddyClimbOutputs outputs) {
        return StatusCode.OK;
    }
}
