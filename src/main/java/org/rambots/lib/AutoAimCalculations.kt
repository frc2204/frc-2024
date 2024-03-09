package org.rambots.lib

class AutoAimCalculations(private val distance: Double) {

    /** Quadratic Regression */

    fun topVelocity(): Double {
        return distance * 9
    }

    fun bottomVelocity(): Double {
        return distance * 10
    }

    fun feedPower(): Double {
        return distance * 8 * 0.1
    }

    fun armPosition(): Double {
        return distance * 0.01
    }

    fun wristPosition(): Double {
        return distance * 0.02
    }
}