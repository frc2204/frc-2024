package org.rambots.auto

import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.util.Units
import org.rambots.util.AllianceFlipUtil

object AutoConstants {

    val pathConstraints = PathConstraints(4.33, 8.0, Units.degreesToRadians(540.0), Units.degreesToRadians(720.0))

    val ampScorePose: Pose2d
        get() = AllianceFlipUtil.apply(
            Pose2d(
                Translation2d(1.82, 7.7),
                Rotation2d.fromDegrees(-90.0)
            )
        )

    val sourceDirectIntake
        get() = SourcePosePositions(
            AllianceFlipUtil.apply(Pose2d(Translation2d(15.83, 1.63), Rotation2d.fromDegrees(120.0))),
            AllianceFlipUtil.apply(Pose2d(Translation2d(15.42, 0.93), Rotation2d.fromDegrees(120.0))),
            AllianceFlipUtil.apply(Pose2d(Translation2d(14.95, 0.64), Rotation2d.fromDegrees(120.0)))
        )

    val sourceGroundIntake
        get() = SourcePosePositions(
            AllianceFlipUtil.apply(Pose2d(Translation2d(1.82, 7.7), Rotation2d.fromDegrees(120.0))),
            AllianceFlipUtil.apply(Pose2d(Translation2d(1.82, 7.7), Rotation2d.fromDegrees(120.0))),
            AllianceFlipUtil.apply(Pose2d(Translation2d(1.82, 7.7), Rotation2d.fromDegrees(120.0)))
        )

    const val CLOSEST_AUTOAIM_DISTANCE = 1.68
    const val FURTHEST_AUTOAIM_DISTANCE = 6.0

}