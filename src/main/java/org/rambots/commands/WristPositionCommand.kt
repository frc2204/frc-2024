package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.WristSubsystem

class WristPositionCommand(private val angle: () -> Double, private val finishCondition: (position: Double) -> Boolean) : Command() {

    constructor(angle: () -> Double) : this(angle, { true })

    init {
        addRequirements(WristSubsystem)
    }

    override fun initialize() {
        WristSubsystem.desiredPosition = angle.invoke()
    }

    override fun execute() {
        WristSubsystem.position = angle.invoke()
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(WristSubsystem.position)
    }

}