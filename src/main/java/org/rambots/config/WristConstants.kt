package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object WristConstants {

    const val WRIST_ID = 21

    val WRIST_PID = PIDConstants(0.07, 0.0, 0.0)

    const val ABSOLUTE_WRIST_OFFSET = 78

}