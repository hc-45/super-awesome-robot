/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.buddyclimb;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyClimbConstants.BuddyClimbSettings;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyIO.BuddyClimbOutputMode;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyIO.BuddyClimbOutputs;
import com.stuypulse.robot.util.FullSubsystem;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.Angle;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class BuddyClimb extends FullSubsystem {
    private final BuddyIO io;
    private final BuddyClimbInputsAutoLogged inputs;
    private final BuddyClimbOutputs outputs;

    @AutoLogOutput(key = "Claw/Pivot/State")
    private BuddyClimbState state;

    public BuddyClimb(final BuddyIO io) {
        super();
        this.io = io;
        this.inputs = new BuddyClimbInputsAutoLogged();
        this.outputs = new BuddyClimbOutputs();

        this.state = BuddyClimbState.RETRACT;
    }

    enum BuddyClimbState {
        /** Braked wherever it is */
        STOP,
        /** Buddy climb is held vertically, right in front of the elevator */
        RETRACT,
        /** Buddy climb is deployed and resting on the bumpers */
        DEPLOY
    }

    private void setState(final BuddyClimbState state) {
        this.state = state;
    }

    private Command commandState(final BuddyClimbState state) {
        return run(coroutine -> setState(state)).named(getName() + "Set" + state.name());
    }

    private final Command stopCommand = commandState(BuddyClimbState.DEPLOY);
    private final Command retractCommand = commandState(BuddyClimbState.RETRACT);
    private final Command deployCommand = commandState(BuddyClimbState.DEPLOY);

    public Command commandStopState() {
        return stopCommand;
    }

    public Command commandRetractState() {
        return retractCommand;
    }

    public Command commandDeployState() {
        return deployCommand;
    }

    // OUTPUT CONTROL
    private void runStop() {
        this.outputs.outputMode = BuddyClimbOutputMode.STOP;
    }

    private void runPosition(Angle targetPosition) {
        this.outputs.outputMode = BuddyClimbOutputMode.POSITION_VOLTAGE;
        this.outputs.targetPosition = targetPosition;
    }
    @Override
    protected void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);

        if (!GlobalSettings.EnabledSubsystems.BUDDY_CLIMB.get()) {
            this.runStop();
            return;
        }

        switch (this.state) {
            case STOP -> this.runStop();
            case RETRACT -> this.runPosition(BuddyClimbSettings.HELD_ANGLE);
            case DEPLOY -> this.runPosition(BuddyClimbSettings.DEPLOYED_ANGLE);
        }
    }

    @Override
    protected void periodicAfterScheduler() {
        io.applyOutputs(outputs);
    }
}
