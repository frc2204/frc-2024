package org.rambots.commands

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.lib.AutoAimCalculations
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.IntakeSubsystem
import org.rambots.subsystems.ShooterSubsystem
import org.rambots.subsystems.WristSubsystem
import org.rambots.subsystems.drive.Drive
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.FieldConstants
import kotlin.math.pow
import kotlin.math.sqrt

class AutoAim(private val drive : Drive, private val driveController: DriveController) : Command() {
    private var topVelocity = 0.0
    private var bottomVelocity = 0.0
    private var feedPower = 0.0
    private var armPosition = 0.0
    private var wristPosition = 0.0
    private val calculator: AutoAimCalculations

    init {
        addRequirements(drive,ShooterSubsystem)

        val current = drive.pose
        val speaker = FieldConstants.Speaker.centerSpeakerOpening.translation
        var distance = (speaker.y - current.y).pow(2) + (speaker.x - current.x).pow(2)
        distance = sqrt(distance)
        calculator = AutoAimCalculations(distance)
    }

    override fun initialize() {
        topVelocity = calculator.topVelocity()
        bottomVelocity = calculator.bottomVelocity()
        feedPower = calculator.feedPower()
        armPosition = calculator.armPosition()
        wristPosition = calculator.wristPosition()

        driveController.setDriveMode(DriveController.DriveModeType.SPEAKER)
        driveController.enableHeadingControl()

        SequentialCommandGroup(
            ShooterSubsystem.shoot(topVelocity, bottomVelocity),
            IntakeSubsystem.feed(feedPower, feedPower)
        ).schedule()
    }

    override fun isFinished(): Boolean {
        driveController.disableHeadingControl()
        return true
    }


    override fun end(interrupted: Boolean) {}
}