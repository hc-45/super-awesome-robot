/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.elevator.Elevator;
import com.stuypulse.robot.subsystems.swerve.Drive;

import org.wpilib.command3.Command;

import org.littletonrobotics.junction.Logger;

public interface CompoundCommands {
    public static Command alignToScoreScale(final Drive drive, final Elevator elevator, final Claw claw) {
        return Command.noRequirements(coroutine -> {
            Logger.recordOutput("Elevator/CommandStatus", "alignToScale");
            coroutine.await(DriveCommands.alignToScale(drive));
            Logger.recordOutput("Elevator/CommandStatus", "elevatorScale");
            coroutine.await(elevator.commandScaleState());
            while (!elevator.isAtScale()) {
                coroutine.yield();
            }
            Logger.recordOutput("Elevator/CommandStatus", "complete");
        }).named("AlignToScoreScale");
    }

    public static Command alignToScoreSwitch(final Drive drive, final Elevator elevator, final Claw claw) {
        return Command.noRequirements(coroutine -> {
            coroutine.await(DriveCommands.alignToSwitch(drive));
            coroutine.await(elevator.commandSwitchState());
            while (!elevator.isAtScale()) {
                coroutine.yield();
            }
        }).named("AlignToScoreSwitch");
    }
}
