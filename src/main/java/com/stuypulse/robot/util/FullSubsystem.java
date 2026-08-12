// Copyright (c) 2025-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.
package com.stuypulse.robot.util;

import java.util.ArrayList;
import java.util.List;

import org.wpilib.command3.Scheduler;

/**
 * A standard subsystem that includes an extra periodic callback which runs
 * after the command
 * scheduler. Allows outputs to be published after all other periodic code has
 * finished.
 */
public abstract class FullSubsystem {
    private static List<FullSubsystem> instances = new ArrayList<>();
    private static final Scheduler DEFAULT_SCHEDULER = Scheduler.getDefault();

    protected FullSubsystem() {
        instances.add(this);
        DEFAULT_SCHEDULER.addPeriodic(this::periodic);
    }

    /**
     * This method is called periodically after the command scheduler, and should be
     * overriden for applying outputs.
     */
    public void periodicAfterScheduler() {};

    /**
     * This method is called periodically before the command scheduler, and should be
     * overriden for processing inputs.
     */
    public abstract void periodic();

    /** Run the "after periodic" methods for all subsystems. */
    public static void runAllPeriodicAfterScheduler() {
        for (FullSubsystem instance : instances) {
            instance.periodicAfterScheduler();
        }
    }
}