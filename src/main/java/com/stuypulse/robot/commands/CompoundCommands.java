/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.elevator.Elevator;
import com.stuypulse.robot.subsystems.swerve.Drive;

import org.wpilib.command3.Command;

public interface CompoundCommands {
    public static Command alignToScoreScale(final Drive drive, final Elevator elevator, final Claw claw) {
        return Command.noRequirements(coroutine -> {
            coroutine.await(DriveCommands.alignToScale(drive));
            coroutine.await(elevator.commandScaleState());
            while (!elevator.isAtScale()) {
                coroutine.yield();
            }
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

    public static Command outtakeIntakeSide(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            coroutine.await(claw.commandPivotOuttakeState());
            while (!claw.isInOuttakePosition()) {
                coroutine.yield();
            }
            coroutine.await(claw.commandRollerOuttakeState());
        }).named("OuttakeIntakeSide");
    }

    public static Command outtakeElevatorSide(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            coroutine.await(claw.commandPivotHeldState());
            while (!claw.isInHeldPosition()) {
                coroutine.yield();
            }
            coroutine.await(claw.commandRollerOuttakeState());
        }).named("OuttakeElevatorSide");
    }
}
