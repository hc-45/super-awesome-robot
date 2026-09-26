/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import static org.wpilib.units.Units.Rotations;

import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;
import com.stuypulse.robot.subsystems.claw.ClawIO.PivotOutputMode;
import com.stuypulse.robot.subsystems.claw.ClawIO.PivotOutputs;
import com.stuypulse.robot.subsystems.claw.ClawIO.RollerOutputMode;
import com.stuypulse.robot.subsystems.claw.ClawIO.RollerOutputs;
import com.stuypulse.robot.util.FullSubsystem;
import com.stuypulse.robot.util.simulation.SimulatedMechanism;

import org.wpilib.command3.Command;
import org.wpilib.math.geometry.Pose3d;
import org.wpilib.math.geometry.Rotation3d;
import org.wpilib.units.measure.*;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Claw extends FullSubsystem implements SimulatedMechanism {
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

    // INTERNAL STATES HANDLING
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

    private Command commandPivotState(final PivotState state) {
        return run(coroutine -> setPivotState(state)).named(getName() + "PivotSet" + state.name());
    }

    private Command commandRollerState(final RollerState state) {
        return run(coroutine -> setRollerState(state)).named(getName() + "RollerSet" + state.name());
    }

    // EXPOSED COMMANDS

    /**
     * Command the pivot to the {@link PivotState#IDLE} state
     * @return Command to orchestrate the target state
     */
    public Command commandPivotIdleState() {
        return this.commandPivotState(PivotState.IDLE);
    }

    /**
     * Command the pivot to the {@link PivotState#INTAKE} state
     * @return Command to orchestrate the target state
     */
    public Command commandPivotIntakeState() {
        return this.commandPivotState(PivotState.INTAKE);
    }

    /**
     * Command the pivot to the {@link PivotState#HELD} state
     * @return Command to orchestrate the target state
     */
    public Command commandPivotHeldState() {
        return this.commandPivotState(PivotState.HELD);
    }

    /**
     * Command the pivot to the {@link PivotState#OUTTAKE} state
     * @return Command to orchestrate the target state
     */
    public Command commandPivotOuttakeState() {
        return this.commandPivotState(PivotState.OUTTAKE);
    }

    /**
     * Command the rollers to the {@link RollerState#IDLE} state
     * @return Command to orchestrate the target state
     */
    public Command comandRollerIdleState() {
        return this.commandRollerState(RollerState.IDLE);
    }

    /**
     * Command the rollers to the {@link RollerState#INTAKE} state
     * @return Command to orchestrate the target state
     */
    public Command commandRollerIntakeState() {
        return this.commandRollerState(RollerState.INTAKE);
    }

    /**
     * Command the rollers to the {@link RollerState#OUTTAKE} state
     * @return Command to orchestrate the target state
     */
    public Command commandRollerOuttakeState() {
        return this.commandRollerState(RollerState.OUTTAKE);
    }

    // EXPOSED INPUTS

    /**
     * @return Whether the pivot is at the deployed angle or not
     */
    public boolean isInDeployedPosition() {
        return inputs.pivotMotorPosition.gt(ClawSettings.Pivot.DEPLOYED_THRESHOLD) && inputs.pivotMotorMotionMagicAtTarget; // kinda sus lock in
    }

    /**
     * @return Whether the pivot is at the held angle or not
     */
    public boolean isInHeldPosition() {
        return inputs.pivotMotorPosition.lt(ClawSettings.Pivot.HELD_THRESHOLD) && inputs.pivotMotorMotionMagicAtTarget; // kinda sus lock in
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
            case INTAKE -> {
                this.runRollerDutyCycle(ClawSettings.Rollers.INTAKE_DUTY_CYCLE);
                this.setCubeState(CubeState.INTAKE);
            }
            case OUTTAKE -> {
                this.runRollerDutyCycle(ClawSettings.Rollers.OUTTAKE_DUTY_CYCLE);
                this.setCubeState(CubeState.STAGE);
            }
        }
    }

    @Override
    protected void periodicAfterScheduler() {
        io.applyPivotOutputs(pivotOutputs);
        io.applyRollerOutputs(rollerOutputs);
    }

    // SIMULATION
    @Override
    public Pose3d getSimulatedPose() {
        return Pose3d.kZero.rotateBy(new Rotation3d(Rotations.zero(), inputs.pivotMotorPosition, Rotations.zero()));
    }

    private enum CubeState {
        STAGE,
        INTAKE;
    }

    private CubeState cubeState = CubeState.STAGE;

    private void setCubeState(final CubeState cubeState) {
        this.cubeState = cubeState;
    }

    public Pose3d getCubePose() {
        return switch(this.cubeState) {
            case STAGE -> Field.STAGING_POSE;
            case INTAKE -> Pose3d.kZero;
        };
    }
}
