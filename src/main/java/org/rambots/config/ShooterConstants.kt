package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object ShooterConstants {

    const val SHOOTER_TOP_ID = 19
    const val SHOOTER_BOTTOM_ID = 20

    val SHOOTER_PID = PIDConstants(0.00085, 0.0000011, 0.1)

    const val SHOOTER_DEFAULT_VELOCITY = 500.0

    const val INTAKE_BOTTOM_ID = 17
    const val INTAKE_TOP_ID = 18

    const val INTAKE_OUTPUT = 0.45

    const val INTAKE_STALL_CURRENT = 30

    const val VELOCITY_ERROR = 100
}