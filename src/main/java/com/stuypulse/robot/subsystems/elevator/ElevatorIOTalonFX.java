/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorPorts;

import com.ctre.phoenix6.hardware.TalonFX;

public final class ElevatorIOTalonFX extends ElevatorIOBase {
    public ElevatorIOTalonFX() {
        super(new TalonFX(ElevatorPorts.TR_MOTOR, GlobalPorts.ELEVATOR),
                new TalonFX(ElevatorPorts.BR_MOTOR, GlobalPorts.ELEVATOR),
                new TalonFX(ElevatorPorts.BL_MOTOR, GlobalPorts.ELEVATOR),
                new TalonFX(ElevatorPorts.TL_MOTOR, GlobalPorts.ELEVATOR));
    }
}
