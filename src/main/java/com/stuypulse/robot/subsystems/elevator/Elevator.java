/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorSettings;
import com.stuypulse.robot.subsystems.elevator.ElevatorIO.ElevatorOutputMode;
import com.stuypulse.robot.subsystems.elevator.ElevatorIO.ElevatorOutputs;
import com.stuypulse.robot.util.FullSubsystem;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.Angle;

import org.littletonrobotics.junction.Logger;

public class Elevator extends FullSubsystem {
    private final ElevatorIO io;
    private final ElevatorInputsAutoLogged inputs;
    private final ElevatorOutputs outputs;

    private ElevatorState state;

    public Elevator(final ElevatorIO io) {
        this.io = io;
        this.inputs = new ElevatorInputsAutoLogged();
        this.outputs = new ElevatorOutputs();

        this.state = ElevatorState.IDLE;
    }

    enum ElevatorState {
        IDLE,
        DOWN,
        SWITCH,
        SCALE
    }

    private void setState(final ElevatorState state) {
        this.state = state;
    }

    private Command commandState(final ElevatorState state) {
        return run(coroutine -> setState(state)).named(getName() + "Set" + state.name());
    }

    private final Command idleCommand = commandState(ElevatorState.IDLE);
    private final Command downCommand = commandState(ElevatorState.DOWN);
    private final Command switchCommand = commandState(ElevatorState.SWITCH);
    private final Command scaleCommand = commandState(ElevatorState.SCALE);

    // Exposed commands
    public Command commandIdle() {
        return idleCommand;
    }

    public Command commandDown() {
        return downCommand;
    }

    public Command commandSwitch() {
        return switchCommand;
    }

    public Command commandScale() {
        return scaleCommand;
    }

    // Output Control
    private void runIdle() {
        this.outputs.outputMode = ElevatorOutputMode.IDLE;
    }

    private void runMotionProfileSetpoint(final Angle setpoint) {
        this.outputs.outputMode = ElevatorOutputMode.MOTION_MAGIC;
        this.outputs.profileSetpoint = setpoint;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);

        if (!GlobalSettings.EnabledSubsystems.ELEVATOR.get()) {
            this.runIdle();
            return;
        }

        switch (this.state) {
            case IDLE -> this.runIdle();
            case DOWN -> this.runMotionProfileSetpoint(ElevatorSettings.STOWED_ANGLE);
            case SWITCH -> this.runMotionProfileSetpoint(ElevatorSettings.SWITCH_ANGLE);
            case SCALE -> this.runMotionProfileSetpoint(ElevatorSettings.SCALE_ANGLE);
        }
    }

    @Override
    protected void periodicAfterScheduler() {
        io.applyOutputs(outputs);
    }
}
