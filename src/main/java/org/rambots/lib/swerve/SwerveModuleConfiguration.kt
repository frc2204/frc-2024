package org.rambots.lib.swerve

import edu.wpi.first.math.geometry.Rotation2d

interface SwerveModuleConfiguration {
    val name: String

    val driveMotorCANId: Int
    val steerMotorCANId: Int
    val steerEncoderCANId: Int
    val steerEncoderOffset: Rotation2d

    val simulationEncoderOffset: Rotation2d
}