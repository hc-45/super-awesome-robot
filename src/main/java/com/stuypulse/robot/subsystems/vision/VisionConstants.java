/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.vision;

import org.wpilib.math.geometry.Transform3d;
import org.wpilib.vision.apriltag.AprilTagFieldLayout;
import org.wpilib.vision.apriltag.AprilTagFields;

public class VisionConstants {
    public interface VisionSettings {
        public static AprilTagFieldLayout APRILTAG_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

        // Basic filtering thresholds
        public static double MAX_AMBIGUITY = 0.3;
        public static double MAX_Z_ERROR = 0.75;

        // Standard deviation baselines, for 1 meter distance and 1 tag
        // (Adjusted automatically based on distance and # of tags)
        public static double LINEAR_STD_DEV_BASELINE = 0.02; // Meters
        public static double ANGULAR_STD_DEV_BASELINE = 0.06; // Radians

        // Multipliers to apply for MegaTag 2 observations
        public static double LINEAR_STD_DEV_MEGATAG2_FACTOR = 0.5; // More stable than full 3D solve
        public static double ANGULAR_STD_DEV_MEGATAG2_FACTOR = Double.POSITIVE_INFINITY; // No rotation data available
    }

    private final record CameraData(String name, Transform3d robotToCamera, double stdDevFactor) {};

    public enum VisionCameras {
        ADRIAN_LI_CAM("Adrian_Li_OV9118", new Transform3d(), 1.0);

        private final CameraData data;
        private VisionCameras(String name, Transform3d robotToCamera, double stdDevFactor) {
            this.data = new CameraData(name, robotToCamera,  stdDevFactor);
        }

        public CameraData getData() {
            return this.data;
        }
    }
}