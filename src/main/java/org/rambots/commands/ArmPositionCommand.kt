package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.ArmConstants.ARM_ACCEPTABLE_ERROR
import org.rambots.subsystems.ArmSubsystem
import org.rambots.util.CommandUtils.generateErrorRange

class ArmPositionCommand(private val angle: () -> Double, private val finishState: () -> Double) : Command() {

    constructor(angle: () -> Double) : this(angle, angle)
    constructor(angle: Double) : this ({angle})

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
        return generateErrorRange(ArmSubsystem.position, ARM_ACCEPTABLE_ERROR).contains(finishState.invoke())
    }

}