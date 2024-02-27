package org.rambots.config

import edu.wpi.first.math.geometry.Rotation2d
import org.rambots.lib.swerve.SwerveModuleConfiguration

enum class SwerveModuleSettings(
    override val steerMotorCANId: Int,
    override val steerEncoderCANId: Int,
    override val driveMotorCANId: Int,
    override val steerEncoderOffset: Rotation2d,
    override val simulationEncoderOffset: Rotation2d
) : SwerveModuleConfiguration {
    /** values to be updated */
    FRONT_LEFT(3, 2, 1, 0.0.degrees, 0.0.degrees),
    FRONT_RIGHT(12, 11, 10, 0.0.degrees, 0.0.degrees),
    BACK_LEFT(6, 5, 4, 0.0.degrees, 0.0.degrees),
    BACK_RIGHT(9, 8, 7, 0.0.degrees, 0.0.degrees),
}

/* Double extension property. Allows to convert a double to a rotation 2d object. */
val Double.degrees: Rotation2d get() = Rotation2d.fromDegrees(this)