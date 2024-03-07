package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.IntakeSubsystem

class Intake : Command() {

    init {
        addRequirements(ArmSubsystem, IntakeSubsystem)
    }

    override fun initialize() {
        SequentialCommandGroup(
            ArmSubsystem.armIntakePosition(),
            ArmSubsystem.wristIntakePosition(),
            IntakeSubsystem.intakeRoll(),
        )
    }

    override fun isFinished(): Boolean {
        return true
    }

    override fun end(interrupted: Boolean) {}
}