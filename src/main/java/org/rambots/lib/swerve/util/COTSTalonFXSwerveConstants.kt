package org.rambots.lib.swerve.util

import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import edu.wpi.first.math.util.Units

class COTSTalonFXSwerveConstants(
    val wheelDiameter: Double,
    val wheelCircumference: Double,
    val angleGearRatio: Double,
    val driveGearRatio: Double,
    val angleKP: Double,
    val angleKI: Double,
    val angleKD: Double,
    val driveMotorInvert: InvertedValue,
    val angleMotorInvert: InvertedValue,
    val cancoderInvert: SensorDirectionValue,
) {
    fun krakenX60(driveGearRatio: Double): COTSTalonFXSwerveConstants {
        val wheelDiameter = Units.inchesToMeters(4.0)

        /* 15.43 : 1 */
        val angleGearRatio = (15.43 / 1)
        val angleKP = 0.15
        val angleKI = 0.0
        val angleKD = 0.0

        val driveMotorInvert = InvertedValue.CounterClockwise_Positive
        val angleMotorInvert = InvertedValue.Clockwise_Positive
        val cancoderInvert = SensorDirectionValue.CounterClockwise_Positive

        return COTSTalonFXSwerveConstants(
            wheelDiameter, wheelDiameter * Math.PI, angleGearRatio, driveGearRatio,
            angleKP, angleKI, angleKD, driveMotorInvert, angleMotorInvert, cancoderInvert
        )
    }
}