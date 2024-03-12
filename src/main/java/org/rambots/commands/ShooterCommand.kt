package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.ShooterSubsystem

class ShooterCommand(private val topVelocity: () -> Double, private val bottomVelocity: () -> Double , private val finishCondition: (topVelocity: Double, bottomVelocity: Double) -> Boolean): Command() {

    constructor(topVelocity: () -> Double, bottomVelocity: () -> Double): this(topVelocity, bottomVelocity, { _,_ -> true })

    init {
        addRequirements(ShooterSubsystem)
    }

    override fun initialize() {
        ShooterSubsystem.topDesiredVelocity = topVelocity.invoke()
        ShooterSubsystem.bottomDesiredVelocity = bottomVelocity.invoke()
    }

    override fun execute() {
        ShooterSubsystem.shoot(topVelocity.invoke(), bottomVelocity.invoke())
    }

    override fun isFinished(): Boolean {
        return finishCondition.invoke(topVelocity.invoke(), bottomVelocity.invoke())
    }

}