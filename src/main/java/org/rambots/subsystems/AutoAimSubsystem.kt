package org.rambots.subsystems

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger

object AutoAimSubsystem: SubsystemBase() {
    private val poseOne = Pose2d(Translation2d(1.0, 2.0), Rotation2d(20.0))
    private val poseTwo = Pose2d(Translation2d(1.0, 2.0), Rotation2d(20.0))

    private val poseList = listOf(poseOne, poseTwo)

    private var closestPose: Pose2d = Pose2d(Translation2d(0.0,0.0), Rotation2d(0.0))

    fun getNearestShotPose(currentPose: Pose2d): Pose2d {
        closestPose = currentPose.nearest(poseList)
        return closestPose
    }

    override fun periodic() {
        Logger.recordOutput("AutoAim/ClosestPose", closestPose)
    }
}