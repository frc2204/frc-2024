package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.config.ArmConstants
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.IntakeSubsystem
import org.rambots.subsystems.SuperstructureSubsystem

class Intake : Command() {

    init {
        addRequirements(SuperstructureSubsystem)
    }

    override fun initialize() {
        SuperstructureSubsystem.intake()
    }

    override fun isFinished(): Boolean {
        return if(IntakeSubsystem.getMotor().outputCurrent >= ArmConstants.IntakeCurrent){
            IntakeSubsystem.stop()
            true
        } else {
            false
        }
    }

    override fun end(interrupted: Boolean) {}
}