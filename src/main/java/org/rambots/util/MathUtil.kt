package org.rambots.util

object MathUtil {

    fun bound(value: Double, min: Double, max: Double): Double {
        return min.coerceAtLeast(max.coerceAtMost(value))
    }

}