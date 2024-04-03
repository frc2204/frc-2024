package org.rambots.util

import com.revrobotics.CANSparkBase.IdleMode

object SparkMaxUtil {

    fun getBrake(brake: Boolean): IdleMode {
        return if (brake) IdleMode.kBrake else IdleMode.kCoast
    }

}