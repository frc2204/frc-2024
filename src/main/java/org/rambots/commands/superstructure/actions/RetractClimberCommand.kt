package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.superstructure.ElevatorSubsystem

class RetractClimberCommand : Command() {

    override fun execute() {
        ElevatorSubsystem.desiredPosition -= 0.5
    }

    override fun isFinished(): Boolean {
        return false
    }

}
