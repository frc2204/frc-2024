package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object ElevatorConstants {

    const val ELEVATOR_LEADER_ID = 15
    const val ELEVATOR_FOLLOWER_ID = 16

    val ELEVATOR_PID = PIDConstants(0.2, 0.0, 0.0)

}