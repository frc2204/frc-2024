package org.rambots.commands

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap
import edu.wpi.first.wpilibj2.command.Command
import org.rambots.auto.AutoConstants.CLOSEST_AUTOAIM_DISTANCE
import org.rambots.auto.AutoConstants.FURTHEST_AUTOAIM_DISTANCE
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.AutoAimSubsystem
import org.rambots.subsystems.LightingSubsystem
import org.rambots.subsystems.superstructure.ShooterSubsystem
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.AllianceFlipUtil
import org.rambots.util.FieldConstants

class AutoAimCommand(private val controller: DriveController, private val pose: () -> Pose2d): Command() {


    private val wristAngle = InterpolatingDoubleTreeMap().apply {
        put(CLOSEST_AUTOAIM_DISTANCE, 72.0)
        put(2.4, -60.0)
        put(2.5, -58.14)
        put(3.98, -42.4)
        put(FURTHEST_AUTOAIM_DISTANCE, 0.0)
    }

    init {
        addRequirements(AutoAimSubsystem)
    }

    override fun initialize() {
//        controller.enableSpeakerHeading()
//        controller.enableHeadingControl()
        ShooterSubsystem.shoot()
    }

    override fun execute() {
        val distance = pose.invoke().translation.getDistance(AllianceFlipUtil.apply(FieldConstants.Speaker.centerSpeakerOpening.translation))
        WristPositionCommand { wristAngle.get(distance) }.schedule()
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {
//        controller.disableHeadingControl()
        WristPositionCommand { 0.0 }.schedule()
        ShooterSubsystem.stopShooter()
    }

}