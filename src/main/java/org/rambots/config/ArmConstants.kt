package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object ArmConstants {

    const val ARM_NEO_LEADER_ID = 13
    const val ARM_NEO_FOLLOWER_ID = 14

    val ARM_PID = PIDConstants(0.02, 0.0, 0.0)

}