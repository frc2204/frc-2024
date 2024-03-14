package org.rambots.subsystems

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.Robot
import org.rambots.RobotContainer
import org.rambots.subsystems.lighting.Blinkin
import org.rambots.subsystems.lighting.BlinkinPattern
import java.awt.Color

object LightingSubsystem : SubsystemBase() {

    private val blinkin = Blinkin(4)

    fun set(pattern: BlinkinPattern) {
        blinkin.set(pattern)
    }

}