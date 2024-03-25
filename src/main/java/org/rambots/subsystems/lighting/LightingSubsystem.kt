package org.rambots.subsystems

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.subsystems.lighting.Blinkin
import org.rambots.subsystems.lighting.BlinkinPattern

object LightingSubsystem : SubsystemBase() {

    private val blinkin = Blinkin(4)
    private var pattern = BlinkinPattern.OFF

    fun set(bp: BlinkinPattern) {
        blinkin.set(bp)
        pattern = bp
    }

    override fun periodic() {
        Logger.recordOutput("Lighting/Pattern", pattern.name)
    }

}