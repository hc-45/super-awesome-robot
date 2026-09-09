/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import com.stuypulse.robot.generated.TunerConstants;

import com.ctre.phoenix6.CANBus;

/** This file contains the different ports of motors, solenoids and sensors */
public interface GlobalPorts {
    CANBus SWERVE = TunerConstants.kCANBus;
    CANBus BUDDY_CLIMB = new CANBus("can_s3");
    CANBus ELEVATOR = new CANBus("can_s0");
    CANBus CLAW = new CANBus("can_s4");

    interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }
}
