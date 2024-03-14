package org.rambots.subsystems.lighting

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.motorcontrol.Spark

class Blinkin(channel: Int) : Spark(channel) {

    fun set(pattern: BlinkinPattern) {
          super.set(pattern.pwmPower)
    }

}