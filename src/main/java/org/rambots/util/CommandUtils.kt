package org.rambots.util

object CommandUtils {

    fun generateErrorRange(value: Double, error: Double): ClosedRange<Double> = (value - error)..(value + error)

}