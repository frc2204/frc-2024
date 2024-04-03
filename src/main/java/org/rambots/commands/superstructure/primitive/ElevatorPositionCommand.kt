package org.rambots.commands.superstructure.primitive

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.superstructure.ElevatorSubsystem

class ElevatorPositionCommand(
    private val position: () -> Double,
    private val finishCondition: (position: Double) -> Boolean
) : Command() {

    constructor(position: () -> Double) : this(position, { true })

    init {
        addRequirements(ElevatorSubsystem)
    }

    override fun execute() {
        ElevatorSubsystem.desiredPosition = position.invoke()
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(ElevatorSubsystem.position)
    }

}