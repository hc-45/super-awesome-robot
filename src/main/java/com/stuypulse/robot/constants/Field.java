/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import static org.wpilib.units.Units.*;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.smartdashboard.Field2d;
import org.wpilib.units.measure.Distance;
import org.wpilib.vision.apriltag.AprilTagFieldLayout;
import org.wpilib.vision.apriltag.AprilTagFields;

import com.stuypulse.robot.Robot;

public interface Field {
    AprilTagFieldLayout APRIL_TAG_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

    Field2d FIELD2D = new Field2d();

    Distance WIDTH = Meters.of(APRIL_TAG_LAYOUT.getFieldWidth());
    Distance LENGTH = Meters.of(APRIL_TAG_LAYOUT.getFieldLength());

    // all placeholders
    Pose2d LEFT_SCALE_CENTER = new Pose2d(Inches.of(182.11), WIDTH.div(2), Rotation2d.kZero);
    Pose2d RIGHT_SCALE_CENTER = new Pose2d(Inches.of(205.6), WIDTH.div(2).plus(Inches.of(47 / 2.0)),
            Rotation2d.kZero);
    Distance SCALE_WIDTH_LENGTH = Inches.of(41.7 / 2.0);

    Pose2d LEFT_SWITCH_CENTER = new Pose2d(Inches.of(42.0), Inches.of(147.47), new Rotation2d());
    Pose2d RIGHT_SWITCH_CENTER = new Pose2d(Inches.of(42.0), Inches.of(147.47 - 23.5 - 10), new Rotation2d());
    Distance SWITCH_WIDTH_LENGTH = Inches.of(6.7);

    public static Pose2d transformToOppositeAlliance(Pose2d pose) {
        Pose2d rotated = pose.rotateBy(Rotation2d.fromDegrees(180));
        return new Pose2d(
                rotated.getTranslation().plus(new Translation2d(LENGTH, WIDTH)),
                rotated.getRotation());
    }

    public static Target getScaleTarget() {
        return Robot.isBlue() ? Target.LEFT_SCALE : Target.RIGHT_SCALE; // not actual method
    }

    public static Target getSwitchTarget() {
        return Robot.isBlue() ? Target.LEFT_SWITCH : Target.RIGHT_SWITCH; // not actual method
    }

    enum Target {
        LEFT_SCALE(LEFT_SCALE_CENTER),
        RIGHT_SCALE(RIGHT_SCALE_CENTER),
        LEFT_SWITCH(LEFT_SWITCH_CENTER),
        RIGHT_SWITCH(RIGHT_SWITCH_CENTER);

        private final Pose2d pose;

        private Target(final Pose2d pose) {
            this.pose = pose;
        }

        public Pose2d getPose() {
            return pose;
        }
    }

    int[] ALL_TAGS = APRIL_TAG_LAYOUT.getTags().stream().mapToInt((tag) -> tag.ID).toArray();
}
