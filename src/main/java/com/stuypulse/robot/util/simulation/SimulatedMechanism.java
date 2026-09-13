/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.util.simulation;

import org.wpilib.math.geometry.Pose3d;

@FunctionalInterface
public interface SimulatedMechanism {
    Pose3d getSimulatedPose();
}
