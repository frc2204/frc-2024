package org.rambots.lib.swerve

import com.ctre.phoenix6.configs.CANcoderConfiguration
import com.ctre.phoenix6.configs.TalonFXConfiguration
import org.rambots.config.SwerveConstants

object SwerveCTREConfigurations {

    val swerveCANcoderConfig = CANcoderConfiguration().apply {
        MagnetSensor.SensorDirection = SwerveConstants.invertedEncoders
    }

    val swerveSteerFalconConfig = TalonFXConfiguration().apply {
        MotorOutput.Inverted = SwerveConstants.steerMotorInvert
        MotorOutput.NeutralMode = SwerveConstants.steerNeutralMode

        Feedback.SensorToMechanismRatio = SwerveConstants.STEER_GEAR_RATIO

        ClosedLoopGeneral.ContinuousWrap = true

        CurrentLimits.SupplyCurrentLimitEnable = SwerveConstants.STEER_ENABLE_CURRENT_LIMIT
        CurrentLimits.SupplyCurrentLimit = SwerveConstants.STEER_CURRENT_LIMIT
        CurrentLimits.SupplyCurrentThreshold = SwerveConstants.STEER_CURRENT_THRESHOLD
        CurrentLimits.SupplyTimeThreshold = SwerveConstants.STEER_CURRENT_THRESHOLD_TIME

        Slot0.kP = SwerveConstants.STEER_KP
        Slot0.kI = SwerveConstants.STEER_KI
        Slot0.kD = SwerveConstants.STEER_KD
    }

    val swerveDriveKrakenConfig = TalonFXConfiguration().apply {
        MotorOutput.Inverted = SwerveConstants.driveMotorInvert
        MotorOutput.NeutralMode = SwerveConstants.driveNeutralMode

        Feedback.SensorToMechanismRatio = SwerveConstants.DRIVE_GEAR_RATIO

        CurrentLimits.SupplyCurrentLimitEnable = SwerveConstants.DRIVE_ENABLE_CURRENT_LIMIT
        CurrentLimits.SupplyCurrentLimit = SwerveConstants.DRIVE_CURRENT_LIMIT
        CurrentLimits.SupplyCurrentThreshold = SwerveConstants.DRIVE_CURRENT_THRESHOLD
        CurrentLimits.SupplyTimeThreshold = SwerveConstants.DRIVE_CURRENT_THRESHOLD_TIME

        Slot0.kP = SwerveConstants.DRIVE_KP
        Slot0.kI = SwerveConstants.DRIVE_KI
        Slot0.kD = SwerveConstants.DRIVE_KD

        OpenLoopRamps.DutyCycleOpenLoopRampPeriod = SwerveConstants.OPEN_LOOP_RAMP
        OpenLoopRamps.VoltageOpenLoopRampPeriod = SwerveConstants.OPEN_LOOP_RAMP

        ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = SwerveConstants.CLOSED_LOOP_RAMP
        ClosedLoopRamps.VoltageClosedLoopRampPeriod = SwerveConstants.CLOSED_LOOP_RAMP
    }
}