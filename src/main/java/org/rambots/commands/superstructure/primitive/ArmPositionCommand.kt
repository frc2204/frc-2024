package org.rambots.commands.superstructure.primitive

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.superstructure.ArmSubsystem

class ArmPositionCommand(private val angle: () -> Double, private val finishCondition: (position: Double) -> Boolean) :
    Command() {

    constructor(angle: () -> Double) : this(angle, { true })

    init {
        addRequirements(ArmSubsystem)
    }

    override fun execute() {
        ArmSubsystem.desiredPosition = angle.invoke()
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(ArmSubsystem.currentPosition)
    }

}