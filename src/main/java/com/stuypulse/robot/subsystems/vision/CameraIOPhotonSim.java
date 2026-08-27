/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.vision;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Transform3d;

import com.stuypulse.robot.subsystems.vision.VisionConstants.VisionSettings;

import java.util.function.Supplier;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;

/** IO implementation for physics sim using PhotonVision simulator. */
public class CameraIOPhotonSim extends CameraIOPhoton {
    private static VisionSystemSim visionSim;

    private final Supplier<Pose2d> poseSupplier;
    private final PhotonCameraSim cameraSim;

    /**
     * Creates a new VisionIOPhotonVisionSim.
     *
     * @param name         The name of the camera.
     * @param poseSupplier Supplier for the robot pose to use in simulation.
     */
    public CameraIOPhotonSim(
            String name, Transform3d robotToCamera, Supplier<Pose2d> poseSupplier) {
        super(name, robotToCamera);
        this.poseSupplier = poseSupplier;

        // Initialize vision sim
        if (visionSim == null) {
            visionSim = new VisionSystemSim("main");
            visionSim.addAprilTags(VisionSettings.APRILTAG_LAYOUT);
        }

        // Add sim camera
        var cameraProperties = new SimCameraProperties();
        cameraSim = new PhotonCameraSim(camera, cameraProperties, VisionSettings.APRILTAG_LAYOUT);
        visionSim.addCamera(cameraSim, robotToCamera);
    }

    @Override
    public void updateInputs(CameraIOInputs inputs) {
        visionSim.update(poseSupplier.get());
        super.updateInputs(inputs);
    }
}
