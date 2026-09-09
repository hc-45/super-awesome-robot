/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;
import com.stuypulse.robot.subsystems.claw.ClawIO.PivotOutputMode;
import com.stuypulse.robot.subsystems.claw.ClawIO.PivotOutputs;
import com.stuypulse.robot.subsystems.claw.ClawIO.RollerOutputMode;
import com.stuypulse.robot.subsystems.claw.ClawIO.RollerOutputs;
import com.stuypulse.robot.util.FullSubsystem;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.*;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Claw extends FullSubsystem {
    private final ClawIO io;
    private final ClawInputsAutoLogged inputs;
    private final PivotOutputs pivotOutputs;
    private final RollerOutputs rollerOutputs;

    @AutoLogOutput(key = "Claw/Pivot/State")
    private PivotState pivotState;
    @AutoLogOutput(key = "Claw/Rollers/State")
    private RollerState rollerState;

    public Claw(final ClawIO io) {
        super();
        this.io = io;
        this.inputs = new ClawInputsAutoLogged();
        this.pivotOutputs = new PivotOutputs();
        this.rollerOutputs = new RollerOutputs();

        this.pivotState = PivotState.IDLE;
        this.rollerState = RollerState.IDLE;
    }

    // STATE
    private enum PivotState {
        /** Pivot stopped wherever it is **/
        IDLE,
        /** Pivot moved to the intake position **/
        INTAKE,
        /** Pivot moved to the held position where it holds a gamepiece **/
        HELD,
        /** Pivot moved to the outtake position **/
        OUTTAKE;
    }

    private void setPivotState(final PivotState state) {
        this.pivotState = state;
    }

    private enum RollerState {
        /** Rollers stopped **/
        IDLE,
        /** Rollers moving inwards to intake a power cube **/
        INTAKE,
        /** Rollers moving outwards to eject a power cube **/
        OUTTAKE;
    }

    private void setRollerState(final RollerState state) {
        this.rollerState = state;
    }

    // INTERNAL COMMANDS
    private Command commandPivotState(final PivotState state) {
        return run(coroutine -> setPivotState(state)).named(getName() + "PivotSet" + state.name());
    }

    private final Command pivotIdleCommand = commandPivotState(PivotState.IDLE);
    private final Command pivotIntakeCommand = commandPivotState(PivotState.INTAKE);
    private final Command pivotHeldCommand = commandPivotState(PivotState.HELD);
    private final Command pivotOuttakeCommand = commandPivotState(PivotState.OUTTAKE);

    private Command commandRollerState(final RollerState state) {
        return run(coroutine -> setRollerState(state)).named(getName() + "RollerSet" + state.name());
    }

    private final Command rollerIdleCommand = commandRollerState(RollerState.IDLE);
    private final Command rollerIntakeCommand = commandRollerState(RollerState.INTAKE);
    private final Command rollerOuttakeCommand = commandRollerState(RollerState.OUTTAKE);

    // EXPOSED COMMANDS
    public Command commandPivotIdle() {
        return pivotIdleCommand;
    }

    public Command commandPivotIntake() {
        return pivotIntakeCommand;
    }

    public Command commandPivotHeld() {
        return pivotHeldCommand;
    }

    public Command commandPivotOuttake() {
        return pivotOuttakeCommand;
    }

    public Command comandRollerIdle() {
        return rollerIdleCommand;
    }

    public Command commandRollerIntake() {
        return rollerIntakeCommand;
    }

    public Command commandRollerOuttake() {
        return rollerOuttakeCommand;
    }

    // PIVOT OUTPUT CONTROL
    private void runPivotIdle() {
        this.pivotOutputs.pivotOutputMode = PivotOutputMode.IDLE;
    }

    private void runMotionProfileSetpoint(Angle setpoint) {
        this.pivotOutputs.pivotOutputMode = PivotOutputMode.MOTION_MAGIC;
        this.pivotOutputs.pivotProfileSetpoint = setpoint;
    }

    // ROLLER OUTPUT CONTROL
    private void runRollerIdle() {
        this.rollerOutputs.rollerOutputMode = RollerOutputMode.IDLE;
    }

    private void runRollerDutyCycle(double targetDutyCycle) {
        this.rollerOutputs.rollerOutputMode = RollerOutputMode.DUTY_CYCLE;
        this.rollerOutputs.rollerTargetDutyCycle = targetDutyCycle;
    }

    @Override
    protected void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);

        if (!GlobalSettings.EnabledSubsystems.CLAW.get()) {
            this.runPivotIdle();
            return;
        }

        switch (this.pivotState) {
            case IDLE -> this.runPivotIdle();
            case INTAKE -> this.runMotionProfileSetpoint(ClawSettings.Pivot.INTAKE_ANGLE);
            case HELD -> this.runMotionProfileSetpoint(ClawSettings.Pivot.HELD_ANGLE);
            case OUTTAKE -> this.runMotionProfileSetpoint(ClawSettings.Pivot.OUTTAKE_ANGLE);
        }

        switch(this.rollerState) {
            case IDLE -> this.runRollerIdle();
            case INTAKE -> this.runRollerDutyCycle(ClawSettings.Rollers.INTAKE_DUTY_CYCLE);
            case OUTTAKE -> this.runRollerDutyCycle(ClawSettings.Rollers.OUTTAKE_DUTY_CYCLE);
        }
    }

    @Override
    protected void periodicAfterScheduler() {
        io.applyPivotOutputs(pivotOutputs);
    }
}
