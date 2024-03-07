package org.rambots.config

class Table(topPower:Double, bottomPower:Double, feedPower:Double, angle:Double) {
    private val tP = topPower
    private val bP = bottomPower
    private val fP = feedPower
    private val angle = angle

    fun getTopPower():Double{
        return tP
    }
    fun getBottomPower():Double{
        return bP
    }
    fun getFeedPower():Double{
        return fP
    }
    fun getAngle():Double{
        return angle
    }
}