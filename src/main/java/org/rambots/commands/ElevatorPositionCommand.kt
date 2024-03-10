package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.ArmConstants.ARM_ACCEPTABLE_ERROR
import org.rambots.subsystems.ElevatorSubsystem
import org.rambots.util.CommandUtils.generateErrorRange

class ElevatorPositionCommand(private val position: () -> Double, private val finishState: () -> Double) : Command() {

    constructor(position: () -> Double) : this(position, position)
    constructor(position: Double) : this ({position})

    init {
        addRequirements(ElevatorSubsystem)
    }

    override fun initialize() {
        ElevatorSubsystem.desiredPosition = position.invoke()
    }

    override fun execute() {
        ElevatorSubsystem.position = position.invoke()
    }

    override fun isFinished(): Boolean {
        return generateErrorRange(ElevatorSubsystem.position, ARM_ACCEPTABLE_ERROR).contains(finishState.invoke())
    }

}