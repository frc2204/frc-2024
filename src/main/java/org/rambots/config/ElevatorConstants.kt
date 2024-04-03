package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object ElevatorConstants {

    const val ELEVATOR_LEADER_ID = 16
    const val ELEVATOR_FOLLOWER_ID = 15

    val ELEVATOR_PID = PIDConstants(0.2, 0.0, 0.0)

    const val ELEVATOR_MAX_HEIGHT = 0.0

}