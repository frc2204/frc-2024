package org.rambots.lib.swerve.util

import edu.wpi.first.math.controller.SimpleMotorFeedforward

data class MotorFeedForwardBuilder(
    val ks: Double,
    val kv: Double,
    val ka: Double,
) {
    fun build(): SimpleMotorFeedforward {
        return SimpleMotorFeedforward(ks, kv, ka)
    }
}