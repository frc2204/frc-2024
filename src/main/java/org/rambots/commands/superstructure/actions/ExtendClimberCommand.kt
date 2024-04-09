package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.ElevatorConstants
import org.rambots.subsystems.superstructure.ElevatorSubsystem

class ExtendClimberCommand : Command() {

    override fun execute() {
        ElevatorSubsystem.desiredPosition += ElevatorConstants.EXTENSION_RATE
    }

    override fun isFinished(): Boolean {
        return false
    }

}
