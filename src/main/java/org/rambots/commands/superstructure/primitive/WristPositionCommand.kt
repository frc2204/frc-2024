package org.rambots.commands.superstructure.primitive

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.subsystems.superstructure.WristSubsystem

class WristPositionCommand(
    private val angle: () -> Double,
    private val finishCondition: (position: Double) -> Boolean
) : Command() {

    constructor(angle: () -> Double) : this(angle, { true })

    init {
        addRequirements(WristSubsystem)
    }

    override fun execute() {
        WristSubsystem.desiredPosition = angle.invoke()
    }

    override fun end(interrupted: Boolean) {
        if (!interrupted && angle.invoke() == 0.0) {
            SequentialCommandGroup(WaitCommand(2.0), Commands.runOnce({
                if (WristSubsystem.desiredPosition == 0.0) WristSubsystem.resetEncoder()
            }))
        }
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(WristSubsystem.position)
    }

}