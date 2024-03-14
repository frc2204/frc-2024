package org.rambots.commands

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.rambots.subsystems.AutoAimSubsystem
import org.rambots.subsystems.drive.Drive
import org.rambots.subsystems.drive.DriveController
import org.rambots.util.AllianceFlipUtil

class AutoAimCommand(private val drive: Drive, private val dController: DriveController): Command() {

    private val closestPose
        get() = AutoAimSubsystem.getNearestShotPose(drive.pose)

    private lateinit var path: Command

    init {
        addRequirements(AutoAimSubsystem)
    }

    override fun initialize() {
        dController.setDriveMode(DriveController.DriveModeType.SPEAKER)
        dController.enableHeadingControl()

        path =
            AutoBuilder.pathfindToPose(
                AllianceFlipUtil.apply(closestPose),
                PathConstraints(4.0, 4.0, Units.degreesToRadians(360.0), Units.degreesToRadians(540.0)),
                0.0,
                0.0
            )
        val scoreCommand = Commands.sequence(path)
        scoreCommand.schedule()
    }

    override fun isFinished(): Boolean {
        return path.isFinished
    }

    override fun end(interrupted: Boolean) {
        path.cancel()
        dController.disableHeadingControl()
    }
}