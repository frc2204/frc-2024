package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.subsystems.ElevatorSubsystem

class Climb : Command() {
    private val elevatorSubsystem = ElevatorSubsystem

    init {
        // each subsystem used by the command must be passed into the addRequirements() method
        addRequirements(elevatorSubsystem)
    }

    override fun initialize() {
        SequentialCommandGroup(
            elevatorSubsystem.extend(),
            WaitCommand(1.0),
            elevatorSubsystem.retract()
        )
    }

    override fun isFinished(): Boolean {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false
    }

    override fun end(interrupted: Boolean) {}
}