package org.rambots.config

import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.NeutralModeValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import edu.wpi.first.math.util.Units
import org.rambots.lib.swerve.util.MotorFeedForwardBuilder

object SwerveConstants {

    /** Swerve CANCoder Configuration */
    /* CCWP -> 0 */
    val invertedEncoders = SensorDirectionValue.CounterClockwise_Positive

    /** Swerve Steering Motor Configurations */

    /* Steer Motor Neutral Mode and Inverts */
    val steerNeutralMode = NeutralModeValue.Coast
    /* CWP -> 1 */
    val steerMotorInvert = InvertedValue.Clockwise_Positive

    /* Steer Motor Gear Ratio */
    const val STEER_GEAR_RATIO = (15.43 / 1)

    /* Steer Motor Current Limiting */
    const val STEER_ENABLE_CURRENT_LIMIT = true
    const val STEER_CURRENT_LIMIT = 25.0
    const val STEER_CURRENT_THRESHOLD = 40.0
    const val STEER_CURRENT_THRESHOLD_TIME = 0.1

    /* Steer PID Config */
    const val STEER_KP = 0.15
    const val STEER_KI = 0.0
    const val STEER_KD = 0.0

    /** Drive Motor Configurations **/
    /* CCWP -> 0 */
    val driveMotorInvert = InvertedValue.CounterClockwise_Positive
    val driveNeutralMode = NeutralModeValue.Brake

    /* Drive Gear Ratio Config */
    const val DRIVE_GEAR_RATIO = (7.36 / 1)

    /* Drive Current Limiting */
    const val DRIVE_ENABLE_CURRENT_LIMIT = true
    const val DRIVE_CURRENT_LIMIT = 35.0
    const val DRIVE_CURRENT_THRESHOLD = 60.0
    const val DRIVE_CURRENT_THRESHOLD_TIME = 0.1

    /* Drive PID Config */
    const val DRIVE_KP = 0.08
    const val DRIVE_KI = 0.0
    const val DRIVE_KD = 0.0

    /* Open and Closed Loop Ramping */
    /*
    * Determines how much time to ramp from 0% output to 100% during open-loop modes
    * Determines how much time to ramp from 0V output to 12V during open-loop modes.
    */
    const val OPEN_LOOP_RAMP = 0.25
    /*
    * Determines how much time to ramp from 0% output to 100% during closed-loop modes
    * Determines how much time to ramp from 0V output to 12V during closed-loop modes.
    */
    const val CLOSED_LOOP_RAMP = 0.0

    val driveFeedForward = MotorFeedForwardBuilder(0.32 / 12, 1.51 / 12, 0.27 / 12)

    private val WHEEL_RADIUS = Units.inchesToMeters(2.0)
    val WHEEL_CIRCUMFERENCE = 2 * Math.PI * WHEEL_RADIUS
    val MAX_SPEED = Units.feetToMeters(15.13)
}