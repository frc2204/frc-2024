package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.LookUpTable
import org.rambots.config.Table
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.AutoAimSubsystem
import org.rambots.subsystems.ShooterSubsystem
import org.rambots.subsystems.drive.Drive
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.FieldConstants
import kotlin.math.pow
import kotlin.math.sqrt

class AutoAim(driveSubsystem: Drive, driveController: DriveController) : Command() {
    val drive = driveSubsystem
    val driveController = driveController
    private var topPower = 0.0
    private var bottomPower = 0.0
    private var feedPower = 0.0
    private var desiredAngle = 0.0
    var armEncoderValue = 0.0
    var wristEncoderValue = 0.0

    init {
        addRequirements(driveSubsystem, AutoAimSubsystem,ShooterSubsystem)
    }

    override fun initialize() {
        val current = drive.pose
        val speaker = FieldConstants.Speaker.centerSpeakerOpening.translation
        var distance = (speaker.y - current.y).pow(2) + (speaker.x - current.x).pow(2)
        distance = sqrt(distance)

        val table = LookUpTable.getTable(distance.toInt())
        topPower = table.getTopPower()
        bottomPower = table.getBottomPower()
        feedPower = table.getFeedPower()
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