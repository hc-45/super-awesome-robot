/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;
import com.stuypulse.robot.subsystems.claw.ClawIO.GripperOutputMode;
import com.stuypulse.robot.subsystems.claw.ClawIO.ClawOutputs;
import com.stuypulse.robot.util.FullSubsystem;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.*;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Claw extends FullSubsystem {
    private final ClawIO io;
    private final ClawInputsAutoLogged inputs;
    private final ClawOutputs outputs;

    @AutoLogOutput(key = "Claw/State")
    private ClawState state;

    private Claw(final ClawIO io) {
        super();
        this.io = io;
        this.inputs = new ClawInputsAutoLogged();
        this.outputs = new ClawOutputs();
    }

    // STATE
    public enum ClawState {
        IDLE,
        OPEN,
        GRAB,
        SQUEEZE;
    }

    private void setState(final ClawState state) {
        this.state = state;
    }

    // EXPOSED COMMANDs
    private Command commandState(final ClawState state) {
        return run(coroutine -> setState(state)).named(getName() + state.name());
    }

    public Command commandIdleState() {
        return commandState(ClawState.IDLE);
    }

    public Command commandOpenState() {
        return commandState(ClawState.OPEN);
    }

    public Command commandGrabState() {
        return commandState(ClawState.GRAB);
    }

    public Command commandSqueezeState() {
        return commandState(ClawState.SQUEEZE);
    }

    // OUTPUT CONTROL
    private void runMotorsIdle() {
        this.outputs.gripperOutputMode = GripperOutputMode.IDLE;
    }


    private void runVoltage(Voltage targetVoltage) {
        this.outputs.gripperOutputMode = GripperOutputMode.VOLTAGE;
        this.outputs.gripperTargetVoltage = targetVoltage;
    }

    private void runMotionProfileSetpoint(Angle setpoint) {
        this.outputs.gripperOutputMode = GripperOutputMode.MOTION_MAGIC;
        this.outputs.gripperProfileSetpoint = setpoint;
    }

    @Override
    protected void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);

        if (!GlobalSettings.EnabledSubsystems.CLAW.get()) {
            this.runMotorsIdle();
            return;
        }

        switch (this.state) {
            case IDLE -> this.runMotorsIdle();
            case OPEN -> this.runMotionProfileSetpoint(ClawSettings.Gripper.OPEN_ANGLE);
            case GRAB -> this.runMotionProfileSetpoint(ClawSettings.Gripper.GRAB_ANGLE);
            case SQUEEZE -> this.runVoltage(ClawSettings.Gripper.SQUEEZE_VOLTAGE);
        }
    }

    @Override
    public void periodicAfterScheduler() {
        io.applyOutputs(outputs);
    }
}
