package org.rambots.lib.math

object Conversions {

    /**
     * @param wheelRPS Wheel Velocity: (in Rotations per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Meters per Second)
     */
    fun RPSToMPS(wheelRPS: Double, circumference: Double): Double {
        return wheelRPS * circumference
    }

    /**
     * @param wheelMPS Wheel Velocity: (in Meters per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Rotations per Second)
     */
    fun MPSToRPS(wheelMPS: Double, circumference: Double): Double {
        return wheelMPS / circumference
    }

    /**
     * @param wheelRotations Wheel Position: (in Rotations)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Distance: (in Meters)
     */
    fun rotationsToMeters(wheelRotations: Double, circumference: Double): Double {
        return wheelRotations * circumference
    }

    /**
     * @param wheelMeters Wheel Distance: (in Meters)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Position: (in Rotations)
     */
    fun metersToRotations(wheelMeters: Double, circumference: Double): Double {
        return wheelMeters / circumference
    }

    /**
     * @param counts Falcon Position Counts
     * @param gearRatio Gear Ratio between Falcon and Mechanism
     * @return Degrees of Rotation of Mechanism
     */
    fun falconToDegrees(positionCounts: Double, gearRatio: Double): Double {
        return positionCounts * (360.0 / (gearRatio * 2048.0))
    }

    /**
     * @param degrees Degrees of rotation of Mechanism
     * @param gearRatio Gear Ratio between Falcon and Mechanism
     * @return Falcon Position Counts
     */
    fun degreesToFalcon(degrees: Double, gearRatio: Double): Double {
        return degrees / (360.0 / (gearRatio * 2048.0))
    }

    /**
     * @param positionCounts Falcon Position Counts
     * @param circumference Circumference of Wheel
     * @param gearRatio Gear Ratio between Falcon and Wheel
     * @return Meters
     */
    fun falconToMeters(positionCounts: Double, circumference: Double, gearRatio: Double): Double {
        return positionCounts * (circumference / (gearRatio * 2048.0))
    }

    fun inchesToMeters(inches: Double): Double {
        return inches * 0.0254
    }
}