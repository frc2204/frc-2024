package org.rambots.commands.superstructure.actions

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap
import edu.wpi.first.wpilibj2.command.Command
import org.rambots.auto.AutoConstants
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.drive.DriveController
import org.rambots.subsystems.superstructure.ShooterSubsystem
import org.rambots.subsystems.superstructure.WristSubsystem
import org.rambots.util.AllianceFlipUtil
import org.rambots.util.FieldConstants

class AutoShootCommand(private val controller: DriveController, private val pose: () -> Pose2d) : Command() {

    init {
        addRequirements(WristSubsystem)
    }

    private val wristAngle = InterpolatingDoubleTreeMap().apply {
        put(AutoConstants.CLOSEST_AUTOAIM_DISTANCE, 68.0)
        put(1.9, 66.5)
        put(2.5, 63.14)
        put(3.98, 42.4)
        put(AutoConstants.FURTHEST_AUTOAIM_DISTANCE, 0.0)
    }

    override fun initialize() {
        controller.enableHeadingControl()
        controller.enableSpeakerHeading()
        ShooterSubsystem.shoot()
    }

    override fun execute() {
        val distance = pose.invoke().translation.getDistance(AllianceFlipUtil.apply(FieldConstants.Speaker.centerSpeakerOpening.translation))
        WristSubsystem.desiredPosition = wristAngle.get(distance)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {
        controller.disableHeadingControl()
        ShooterSubsystem.stopShooter()
        WristSubsystem.home()
    }

}
