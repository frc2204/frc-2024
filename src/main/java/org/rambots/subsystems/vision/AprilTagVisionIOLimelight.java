// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.rambots.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import org.littletonrobotics.junction.Logger;
import org.rambots.Robot;
import org.rambots.subsystems.drive.Drive;
import org.rambots.util.LimelightHelpers;
import org.rambots.util.VisionHelpers;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents the implementation of AprilTagVisionIO using Limelight camera.
 */
public class AprilTagVisionIOLimelight implements AprilTagVisionIO {

    private final StringSubscriber observationSubscriber;
    String limelightName;
    Drive drive;

    /**
     * Constructs a new AprilTagVisionIOLimelight instance.
     *
     * @param identifier The identifier of the Limelight camera.
     */
    public AprilTagVisionIOLimelight(String identifier, Drive drive) {
        limelightName = identifier;
        this.drive = drive;

        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable(identifier);
        LimelightHelpers.setPipelineIndex(limelightName, 0);

        observationSubscriber = limelightTable.getStringTopic("json").subscribe("", PubSubOption.keepDuplicates(true), PubSubOption.sendAll(true));
    }

    /**
     * Updates the inputs for AprilTag vision.
     *
     * @param inputs The AprilTagVisionIOInputs object containing the inputs.
     */
    @Override
    public void updateInputs(AprilTagVisionIOInputs inputs) {
        TimestampedString[] queue = observationSubscriber.readQueue(); // Reads the queue of timestamped strings
        ArrayList<VisionHelpers.PoseEstimate> poseEstimates = new ArrayList<>(); // Creates an empty ArrayList to store pose estimates

        // Iterates over each timestamped string in the queue
        for (int i = 0; i < Math.min(queue.length, 3); i++) {
            TimestampedString timestampedString = queue[i];

            // Converts the timestamp to seconds
            double timestamp = timestampedString.timestamp / 1e6;

            // Parses the JSON dump and retrieves the targeting results
            LimelightHelpers.Results results = LimelightHelpers.parseJSONDump(timestampedString.value).targetingResults;

            // Retrieves the alliance information from the DriverStation
            Optional<Alliance> allianceOptional = DriverStation.getAlliance();

            // Checks if there are no targets or if the alliance information is not present
            if (results.targets_Fiducials.length == 0 || !allianceOptional.isPresent()) {
                continue; // Skips to the next iteration of the loop
            }

            double latencyMS = results.latency_capture + results.latency_pipeline; // Calculates the total latency in milliseconds
            Pose3d poseEstimation = results.getBotPose3d_wpiBlue(); // Retrieves the pose estimation for the robot

            double averageTagDistance = 0.0; // Initializes the average tag distance to 0.0
            timestamp -= (latencyMS / 1e3); // Adjusts the timestamp by subtracting the latency in seconds

            int[] tagIDs = new int[results.targets_Fiducials.length]; // Creates an array to store the tag IDs

            // Iterates over each target in the targeting results
            for (int aprilTags = 0; aprilTags < results.targets_Fiducials.length; aprilTags++) {
                // Retrieves and stores the tag ID
                tagIDs[aprilTags] = (int) results.targets_Fiducials[aprilTags].fiducialID;
                averageTagDistance +=
                        results
                                .targets_Fiducials[aprilTags]
                                .getTargetPose_CameraSpace()
                                .getTranslation()
                                .getNorm(); // Calculates the sum of the tag distances
            }

            averageTagDistance /= tagIDs.length; // Calculates the average tag distance
            poseEstimates.add(new VisionHelpers.PoseEstimate(
                    poseEstimation,
                    timestamp,
                    averageTagDistance,
                    tagIDs
            )); // Creates a new PoseEstimate object and adds it to the poseEstimates
            // ArrayList
        }

        // Assigns the poseEstimates ArrayList to the inputs.poseEstimates variable
        inputs.poseEstimates = poseEstimates;
    }
}
