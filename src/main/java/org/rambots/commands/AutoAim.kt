package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.LookUpTable
import org.rambots.subsystems.ShooterSubsystem
import org.rambots.subsystems.drive.Drive
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.FieldConstants
import kotlin.math.pow
import kotlin.math.sqrt

class AutoAim(private val drive : Drive, private val driveController: DriveController) : Command() {
    private var topVelocity = 0.0
    private var bottomVelocity = 0.0
    private var feedVelocity = 0.0
    private var desiredAngle = 0.0
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
        desiredAngle = table.getAngle()

        driveController.setDriveMode(DriveController.DriveModeType.SPEAKER)
        driveController.enableHeadingControl()
    }

    override fun execute() {

    }

    override fun isFinished(): Boolean {
        driveController.disableHeadingControl()
        return true
    }


    override fun end(interrupted: Boolean) {}
}