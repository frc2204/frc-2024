package org.rambots.config

class Table(topVelocity:Double, bottomVelocity:Double, feedVelocity:Double, angle:Double) {
    private val tP = topVelocity
    private val bP = bottomVelocity
    private val fP = feedVelocity
    private val angle = angle

    fun getTopVelocity():Double{
        return tP
    }
    fun getBottomVelocity():Double{
        return bP
    }
    fun getFeedVelocity():Double{
        return fP
    }
    fun getAngle():Double{
        return angle
    }
}