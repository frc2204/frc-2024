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
    FRONT_LEFT(1, 2, 3, 0.0.degrees, 0.0.degrees),
    FRONT_RIGHT(4, 5, 6, 0.0.degrees, 0.0.degrees),
    BACK_LEFT(10, 11, 12, 0.0.degrees, 0.0.degrees),
    BACK_RIGHT(7, 8, 9, 0.0.degrees, 0.0.degrees),
}

/* Double extension property. Allows to convert a double to a rotation 2d object. */
val Double.degrees: Rotation2d get() = Rotation2d.fromDegrees(this)