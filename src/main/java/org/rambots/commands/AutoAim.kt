package org.rambots.commands

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.rambots.auto.AutoConstants
import org.rambots.config.LookUpTable
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.IntakeSubsystem
import org.rambots.subsystems.ShooterSubsystem
import org.rambots.subsystems.WristSubsystem
import org.rambots.subsystems.drive.Drive
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.FieldConstants
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class AutoAim(private val drive : Drive, private val driveController: DriveController) : Command() {
    private var topVelocity = 0.0
    private var bottomVelocity = 0.0
    private var feedVelocity = 0.0
    private var desiredPose: Pose2d = TODO()
    private var pathRun: Command = TODO()
    private var score: Command = TODO()
    var armEncoderValue = 0.0
    var wristEncoderValue = 0.0

    init {
        addRequirements(drive,ShooterSubsystem)
    }

    override fun initialize() {
        val current = drive.pose
        val speaker = FieldConstants.Speaker.centerSpeakerOpening.translation
        var distance = (speaker.y - current.y).pow(2) + (speaker.x - current.x).pow(2)
        distance = sqrt(distance)

        val table = LookUpTable.getTable(distance.toInt())
        topVelocity = table.getTopVelocity()
        bottomVelocity = table.getBottomVelocity()
        feedVelocity = table.getFeedVelocity()
        armEncoderValue = table.getArm()
        wristEncoderValue = table.getWrist()

        ArmSubsystem.setArmPosition(armEncoderValue)
        WristSubsystem.setWristPosition(wristEncoderValue)

        driveController.setDriveMode(DriveController.DriveModeType.SPEAKER)
        driveController.enableHeadingControl()

        val x = distance.toInt() * cos(drive.pose.rotation.degrees)
        val y = distance.toInt() * sin(drive.pose.rotation.degrees)
        desiredPose = Pose2d(Translation2d(x, y), drive.pose.rotation)

        pathRun = AutoBuilder.pathfindToPose(desiredPose, AutoConstants.pathConstraints)

        score = Commands.sequence(pathRun)
        score.schedule()
    }

    override fun execute() {
        IntakeSubsystem.feed(feedVelocity,feedVelocity)
        ShooterSubsystem.shoot(topVelocity, bottomVelocity)
    }

    override fun isFinished(): Boolean {
        driveController.disableHeadingControl()
        return score.isFinished
    }


    override fun end(interrupted: Boolean) { score.cancel() }
}