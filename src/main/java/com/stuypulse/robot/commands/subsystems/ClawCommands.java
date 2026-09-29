package com.stuypulse.robot.commands.subsystems;

// import org.littletonrobotics.junction.Logger;
import org.wpilib.command3.Command;

import com.stuypulse.robot.subsystems.claw.Claw;

public interface ClawCommands {
    

    public static Command outtakeIntakeSide(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            coroutine.await(claw.commandPivotOuttakeState());
            while (!claw.isInDeployedPosition()) {
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

    public static Command intake(final Claw claw) {
        return Command.requiring(claw).executing(coroutine -> {
            coroutine.await(claw.commandPivotIntakeState());
            // Logger.recordOutput("Claw/CommandStatus", "intakeState");
            while (!claw.isInDeployedPosition()) {
                // Logger.recordOutput("Claw/CommandStatus", "yieldForDeploy");
                coroutine.yield();
            }
            // Logger.recordOutput("Claw/CommandStatus", "outtakeState");
            coroutine.await(claw.commandRollerIntakeState());
        }).named("IntakeElevatorSide");
    }
}
