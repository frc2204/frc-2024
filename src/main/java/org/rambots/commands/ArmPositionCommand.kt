package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.ArmSubsystem

class ArmPositionCommand(private val angle: () -> Double, private val finishCondition: (position: Double) -> Boolean) : Command() {

    constructor(angle: () -> Double) : this(angle, { true })

    init {
        addRequirements(ArmSubsystem)
    }

    override fun initialize() {
        ArmSubsystem.desiredPosition = angle.invoke()
    }

    override fun execute() {
        ArmSubsystem.position = angle.invoke()
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(ArmSubsystem.position)
    }

}