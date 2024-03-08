package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.subsystems.SuperstructureSubsystem

class Climb : Command() {
    init {
        // each subsystem used by the command must be passed into the addRequirements() method
        addRequirements(SuperstructureSubsystem)
    }

    override fun initialize() {
        SuperstructureSubsystem.climb()
    }

    override fun isFinished(): Boolean {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false
    }

    override fun end(interrupted: Boolean) {}
}