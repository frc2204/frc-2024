// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.rambots.subsystems.vision;

import edu.wpi.first.math.geometry.Pose3d;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;
import org.rambots.util.LimelightHelpers;
import org.rambots.util.VisionHelpers;

import java.util.ArrayList;

public interface AprilTagVisionIO {
    default void updateInputs(AprilTagVisionIOInputs inputs) {
    }

    class AprilTagVisionIOInputs implements LoggableInputs {

        ArrayList<VisionHelpers.PoseEstimate> poseEstimates = new ArrayList<>();

        @Override
        public void toLog(LogTable table) {
            table.put("poseEstimates", poseEstimates.size());
            for (VisionHelpers.PoseEstimate poseEstimate : poseEstimates) {
                int posePosition = poseEstimates.indexOf(poseEstimate);
                table.put(
                        "estimatedPose/" + Integer.toString(posePosition),
                        VisionHelpers.getPose3dToArray(poseEstimate.pose()));
                table.put(
                        "captureTimestamp/" + Integer.toString(posePosition), poseEstimate.timestampSeconds());
                table.put("tagIDs/" + Integer.toString(posePosition), poseEstimate.tagIDs());
                table.put(
                        "averageTagDistance/" + Integer.toString(posePosition),
                        poseEstimate.averageTagDistance());
            }
            table.put("valid", !poseEstimates.isEmpty());
        }

        @Override
        public void fromLog(LogTable table) {
            int estimatedPoseCount = table.get("poseEstimates", 0);
            for (int i = 0; i < estimatedPoseCount; i++) {
                Pose3d poseEstimation =
                        LimelightHelpers.toPose3D(
                                table.get("estimatedPose/" + Integer.toString(i), new double[]{}));
                double timestamp = table.get("captureTimestamp/" + Integer.toString(i), 0.0);
                double averageTagDistance = table.get("averageTagDistance/" + Integer.toString(i), 0.0);
                int[] tagIDs = table.get("tagIDs/" + Integer.toString(i), new int[]{});
                poseEstimates.add(
                        new VisionHelpers.PoseEstimate(poseEstimation, timestamp, averageTagDistance, tagIDs));
            }
            table.get("valid", false);
        }
    }
}
