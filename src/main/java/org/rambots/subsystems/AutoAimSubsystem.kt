package org.rambots.subsystems

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.rambots.config.ArmConstants.SpeakerGoalHeightInches
import org.rambots.config.ArmConstants.LimelightMountAngleDegrees
import org.rambots.config.ArmConstants.LimelightLensHeightInches
import org.rambots.config.LookUpTable.getTable
import org.rambots.lib.LimelightHelpers
import org.rambots.lib.math.Conversions
import kotlin.math.tan

object AutoAimSubsystem:SubsystemBase() {
    var desiredPose:Pose2d = SwerveSubsystem.pose
    var topPower = 0.0
    var bottomPower = 0.0
    var feedPower = 0.0
    private var desiredAngle = 0.0
    var armEncoderValue = 0.0
    var wristEncoderValue = 0.0

    private val table = NetworkTableInstance.getDefault().getTable("limelight")

    override fun periodic(){
        val ty = table.getEntry("ty")
        val targetOffsetAngleVertical = ty.getDouble(0.0)
        val angleToGoalDegrees = LimelightMountAngleDegrees + targetOffsetAngleVertical
        val angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0)
        val distanceMeters = Conversions.inchesToMeters(
            (SpeakerGoalHeightInches - LimelightLensHeightInches) / tan(angleToGoalRadians)
        ).toInt()

        val table = getTable(distanceMeters)

        topPower = table.getTopPower()
        bottomPower = table.getBottomPower()
        feedPower = table.getFeedPower()
        desiredAngle = table.getAngle()
        armEncoderValue
        wristEncoderValue
    }
}