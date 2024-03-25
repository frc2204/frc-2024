package org.rambots.commands

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.rambots.auto.AutoConstants

class SourceIntake: Command() {
    private val pathRun get() = AutoBuilder.pathfindToPose(AutoConstants.sourceDirectIntake.left, AutoConstants.pathConstraints)
    private var scoreCommand = Commands.sequence(pathRun)

    override fun initialize() {
        super.initialize()
        scoreCommand = Commands.sequence(pathRun)
        scoreCommand.schedule()
    }

    override fun end(interrupted: Boolean) {
        super.end(interrupted)
        scoreCommand.cancel()
    }

    override fun isFinished(): Boolean {
        return scoreCommand.isFinished
    }
}