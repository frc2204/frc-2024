package org.rambots.lib.swerve

import com.ctre.phoenix6.controls.DutyCycleOut
import com.ctre.phoenix6.controls.PositionVoltage
import com.ctre.phoenix6.controls.VelocityVoltage
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.rambots.Robot
import org.rambots.config.SwerveConstants
import org.rambots.lib.math.Conversions.degreesToFalcon
import org.rambots.lib.math.Conversions.falconToDegrees
import org.rambots.lib.math.Conversions.rotationsToMeters
import org.rambots.lib.math.Conversions.MPSToRPS

class SwerveModule(val config: SwerveModuleConfiguration) {

    private val absoluteEncoder = CANcoder(config.steerEncoderCANId)
    private val driveMotor = TalonFX(config.driveMotorCANId)
    private val steerMotor = TalonFX(config.steerMotorCANId)

    private val driveFeedForward = SwerveConstants.driveFeedForward.build()

    private val angleOffset = if (Robot.real) config.steerEncoderOffset else config.simulationEncoderOffset

    /* help to optimize turns */
    private lateinit var lastAngle: Rotation2d

    /* drive motor control requests */
    private val driveDutyCycle = DutyCycleOut(0.0)
    private val driveVelocity = VelocityVoltage(0.0)

    /* angle motor control requests */
    private val anglePosition = PositionVoltage(0.0)

    init {
        absoluteEncoder.apply {
            absoluteEncoder.configurator.apply(SwerveCTREConfigurations.swerveCANcoderConfig)
        }

        steerMotor.apply {
            steerMotor.configurator.apply(SwerveCTREConfigurations.swerveSteerFalconConfig)
            // resets values of steer motor
            resetToAbsolute()
        }

        driveMotor.apply {
            driveMotor.configurator.apply(SwerveCTREConfigurations.swerveDriveKrakenConfig)
            // sets position to 0
            driveMotor.configurator.setPosition(0.0)
        }
        lastAngle = angle()
    }

    fun setDesiredState(desiredState: SwerveModuleState, isOpenLoop: Boolean) {
        val optimizedState = SwerveModuleState.optimize(desiredState, getState().angle)
        /* sets motor's position setpoint */
        steerMotor.setControl(anglePosition.withPosition(optimizedState.angle.rotations))
        setSpeed(optimizedState, isOpenLoop)

        SmartDashboard.putNumber("Swerve Desired: ${config.name} Speed", desiredState.speedMetersPerSecond)
        SmartDashboard.putNumber("Swerve Desired: ${config.name} Angle", desiredState.angle.degrees)
        SmartDashboard.putNumber(
            "Swerve Optimized Desired: ${config.name} Speed",
            optimizedState.speedMetersPerSecond
        )
        SmartDashboard.putNumber("Swerve Optimized Desired: ${config.name} Angle", optimizedState.angle.degrees)
    }

    private fun setSpeed(desiredState: SwerveModuleState, isOpenLoop: Boolean) {
        if (isOpenLoop) {
            /* proportion of supply voltage to apply in fractional units between -1 and +1 */
            driveDutyCycle.Output = desiredState.speedMetersPerSecond / SwerveConstants.MAX_SPEED
            driveMotor.setControl(driveDutyCycle)
        }
        else {
            driveVelocity.Velocity = MPSToRPS(desiredState.speedMetersPerSecond, SwerveConstants.WHEEL_CIRCUMFERENCE)
            driveVelocity.FeedForward = driveFeedForward.calculate(desiredState.speedMetersPerSecond)
            driveMotor.setControl(driveVelocity)
        }
    }

    private fun angle(): Rotation2d {
        return Rotation2d.fromDegrees(falconToDegrees(steerMotor.position.value, SwerveConstants.STEER_GEAR_RATIO))
    }

    /*
    * Returns:
    * Speed of the wheel of the module
    * Angle of the module
    */
    fun getState(): SwerveModuleState {
        return SwerveModuleState(
            rotationsToMeters(driveMotor.velocity.value, SwerveConstants.WHEEL_CIRCUMFERENCE),
            Rotation2d.fromRotations(steerMotor.position.value)
        )
    }

    /*
    * Returns: Rotation2d value from position of absolute encoder (degrees)
    */
    private fun getCANcoder(): Rotation2d {
        return Rotation2d.fromDegrees(absoluteEncoder.absolutePosition.value)
    }
    private fun resetToAbsolute() {
        val absolutePosition = degreesToFalcon(getCANcoder().degrees - config.steerEncoderOffset.degrees, SwerveConstants.STEER_GEAR_RATIO)
        steerMotor.setPosition(absolutePosition)
    }
}