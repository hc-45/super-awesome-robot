/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import static org.wpilib.units.Units.Rotations;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorSettings;
import com.stuypulse.robot.subsystems.elevator.ElevatorIO.ElevatorOutputMode;
import com.stuypulse.robot.subsystems.elevator.ElevatorIO.ElevatorOutputs;
import com.stuypulse.robot.util.FullSubsystem;
import com.stuypulse.robot.util.simulation.SimulatedMechanism;

import org.wpilib.command3.Command;
import org.wpilib.math.geometry.Pose3d;
import org.wpilib.math.geometry.Rotation3d;
import org.wpilib.units.measure.Angle;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Elevator extends FullSubsystem implements SimulatedMechanism {
    private final ElevatorIO io;
    private final ElevatorInputsAutoLogged inputs;
    private final ElevatorOutputs outputs;

    @AutoLogOutput(key = "Elevator/State")
    private ElevatorState state;

    public Elevator(final ElevatorIO io) {
        this.io = io;
        this.inputs = new ElevatorInputsAutoLogged();
        this.outputs = new ElevatorOutputs();

        this.state = ElevatorState.IDLE;
    }

    // INTERNAL STATE HANDLING
    private enum ElevatorState {
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

    // EXPOSED COMMANDS

    /**
     * Command the elevator to the {@link ElevatorState#IDLE} state
     * @return Command to orchestrate the target state
     */
    public Command commandIdleState() {
        return this.commandState(ElevatorState.IDLE);
    }

    /**
     * Command the elevator to the {@link ElevatorState#DOWN} state
     * @return Command to orchestrate the target state
     */
    public Command commandDownState() {
        return this.commandState(ElevatorState.DOWN);
    }

    /**
     * Command the elevator to the {@link ElevatorState#SWITCH} state
     * @return Command to orchestrate the target state
     */
    public Command commandSwitchState() {
        return this.commandState(ElevatorState.SWITCH);
    }

    /**
     * Command the elevator to the {@link ElevatorState#SCALE} state
     * @return Command to orchestrate the target state
     */
    public Command commandScaleState() {
        return this.commandState(ElevatorState.SCALE);
    }

    // EXPOSED INPUTS

    public boolean isAtScale() {
        return inputs.TRMotorPosition.isNear(ElevatorSettings.SCALE_ANGLE, ElevatorSettings.SCALE_TOLERANCE) && inputs.TRMotorMotionMagicAtTarget; // kinda sus lock in
    }

    public boolean isAtSwitch() {
        return inputs.TRMotorPosition.isNear(ElevatorSettings.SWITCH_ANGLE, ElevatorSettings.SWITCH_TOLERANCE) && inputs.TRMotorMotionMagicAtTarget; // kinda sus lock in
    }

    // OUTPUT CONTROL
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

    @Override
    public Pose3d getSimulatedPose() {
        Logger.recordOutput("Elevator/MetersPerRotation", ElevatorSettings.METERS_PER_ROTATION);
        return new Pose3d(0,0, ElevatorSettings.METERS_PER_ROTATION * inputs.TRMotorPosition.in(Rotations), Rotation3d.kZero);
    }
}
