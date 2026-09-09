/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawPorts;

public final class ClawIOTalonFX extends ClawIOBase {
    public ClawIOTalonFX() {
        super(new TalonFX(ClawPorts.CLAW_PIVOT_MOTOR, GlobalPorts.ELEVATOR), new TalonFX(ClawPorts.CLAW_ROLLER_MOTOR, GlobalPorts.ELEVATOR));
    }
}
