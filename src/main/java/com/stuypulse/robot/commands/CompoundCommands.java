package com.stuypulse.robot.commands;

import org.wpilib.command3.Command;

import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.elevator.Elevator;
import com.stuypulse.robot.subsystems.swerve.Drive;

public interface CompoundCommands {
    public static Command alignToScoreScale(final Drive drive, final Elevator elevator, final Claw claw) {
        return Command.noRequirements(coroutine -> {
            coroutine.await(DriveCommands.alignToScale(drive));
            elevator.commandScale();
            while (!elevator.isAtScale()) {
                coroutine.yield();
            }
        }).named("AlignToScoreScale");
    }

    public static Command alignToScoreSwitch(final Drive drive, final Elevator elevator, final Claw claw) {
        return Command.noRequirements(coroutine -> {
            coroutine.await(DriveCommands.alignToSwitch(drive));
            elevator.commandSwitch();
            while (!elevator.isAtScale()) {
                coroutine.yield();
            }
        }).named("AlignToScoreSwitch");
    }

    public static Command outtakeIntakeSide(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            claw.commandPivotOuttake();
            while (!claw.isInOuttakePosition()) {
                coroutine.yield();
            }
            claw.commandRollerOuttake();
        }).named("OuttakeIntakeSide");
    }

    public static Command outtakeElevatorSide(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            claw.commandPivotHeld();
            while (!claw.isInHeldPosition()) {
                coroutine.yield();
            }
            claw.commandRollerOuttake();
        }).named("OuttakeElevatorSide");
    }
}
