package org.rambots.commands.superstructure.primitive

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.superstructure.ShooterSubsystem

class IntakeCommand : Command() {

    override fun initialize() {
        ShooterSubsystem.intake()
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {
        ShooterSubsystem.stopIntake()
    }

}
