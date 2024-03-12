package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object WristConstants {

    const val WRIST_ID = 21

    val WRIST_PID = PIDConstants(0.04, 0.0, 0.0)

    const val WRIST_INTAKE_POSITION = -116.0

}