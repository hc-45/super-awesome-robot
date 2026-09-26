/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.util.simulation;

import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.subsystems.claw.Claw;
import com.stuypulse.robot.subsystems.elevator.Elevator;
import com.stuypulse.robot.subsystems.swerve.Drive;

import org.wpilib.command3.Scheduler;
import org.wpilib.math.geometry.Pose3d;
import org.wpilib.math.geometry.Rotation3d;
import org.wpilib.math.geometry.Transform3d;

import org.littletonrobotics.junction.Logger;

public class Simulation {
    private final Drive drive;
    private final Elevator elevator;
    private final Claw claw;

    public Simulation(final Drive drive, final Elevator elevator, final Claw claw) {
        this.drive = drive;
        this.elevator = elevator;
        this.claw = claw;

        Scheduler.getDefault().addPeriodic(this::periodic);
    }

    private void periodic() {
        final Pose3d movingStagePose = elevator.getSimulatedPose();
        final double carriageZ = movingStagePose.getZ() * 2;
        final Pose3d carriagePose = new Pose3d(0,0, carriageZ, new Rotation3d());
        final Pose3d clawPose = SimulationConstants.CLAW_OFFSETS.applyToPose3d(new Pose3d(0, 0, movingStagePose.getZ() * 2, claw.getSimulatedPose().getRotation()));
        final Pose3d cubePose = claw.getCubePose().plus(new Transform3d(clawPose.getX(), clawPose.getY(), clawPose.getZ(), clawPose.getRotation())); // sus if staged

        Logger.recordOutput("AdvScope/DTPose", drive.getSimulatedPose());
        Logger.recordOutput("AdvScope/MovingStagePose", movingStagePose);
        Logger.recordOutput("AdvScope/CarriagePose", carriagePose);
        Logger.recordOutput("AdvScope/ClawPose", clawPose);
        Logger.recordOutput("AdvScope/CubePose", cubePose);

        Logger.recordOutput("AdvScope/ScalePose", new Pose3d(Field.LEFT_SCALE_CENTER));
    }
}
