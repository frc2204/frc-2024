package org.rambots.config

class Table(topVelocity:Double, bottomVelocity:Double, feedVelocity:Double, arm:Double,wrist:Double) {
    private val tP = topVelocity
    private val bP = bottomVelocity
    private val fP = feedVelocity
    private val armEncoderValue = arm
    private val wristEncoderValue = wrist

    fun getTopVelocity():Double{
        return tP
    }
    fun getBottomVelocity():Double{
        return bP
    }
    fun getFeedVelocity():Double{
        return fP
    }
    fun getArm():Double{
        return armEncoderValue
    }
    fun getWrist():Double{
        return wristEncoderValue
    }
}