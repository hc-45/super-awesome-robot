/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.util;

import org.wpilib.command3.Mechanism;
import org.wpilib.command3.Scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * A standard subsystem that includes an extra periodic callback which runs
 * after the command
 * scheduler. Allows outputs to be published after all other periodic code has
 * finished.
 */
public abstract class FullSubsystem extends Mechanism {
    private static final List<FullSubsystem> subsystemInstances = new ArrayList<>();
    private static final Scheduler DEFAULT_SCHEDULER = Scheduler.getDefault();

    protected FullSubsystem() {
        super();
        subsystemInstances.add(this);
        DEFAULT_SCHEDULER.addPeriodic(this::periodic);
    }

    protected FullSubsystem(String name) {
        super(name);
        subsystemInstances.add(this);
        DEFAULT_SCHEDULER.addPeriodic(this::periodic);
    }

    /**
     * This method is called periodically after {@link Scheduler#run}, and should be
     * overriden for applying outputs.
     */
    protected void periodicAfterScheduler() {};

    /**
     * This method is called periodically before {@link Scheduler#run}, and should be
     * overriden for processing inputs.
     */
    protected abstract void periodic();

    /** Run the {@link #periodicAfterScheduler} methods for all subsystems. */
    public static void runAllPeriodicAfterScheduler() {
        for (FullSubsystem instance : subsystemInstances) {
            instance.periodicAfterScheduler();
        }
    }
}
