/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.buddyclimb;

import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyClimbConstants.BuddyClimbPorts;

import com.ctre.phoenix6.hardware.TalonFX;

public final class BuddyIOSim extends BuddyIOBase {
    public BuddyIOSim() {
        super(new TalonFX(BuddyClimbPorts.WINCH_MOTOR, GlobalPorts.BUDDY_CLIMB));
    }
}
