package org.rambots.lib.swerve

import com.ctre.phoenix6.controls.DutyCycleOut
import com.ctre.phoenix6.controls.PositionVoltage
import com.ctre.phoenix6.controls.VelocityVoltage
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.rambots.Robot
import org.rambots.config.SwerveConstants
import org.rambots.config.SwerveConstants.WHEEL_CIRCUMFERENCE
import org.rambots.lib.math.Conversions.MPSToRPS
import org.rambots.lib.math.Conversions.degreesToFalcon
import org.rambots.lib.math.Conversions.falconToDegrees
import org.rambots.lib.math.Conversions.rotationsToMeters

class SwerveModule(private val config: SwerveModuleConfiguration) {

    private val absoluteEncoder = CANcoder(config.steerEncoderCANId)
    private val driveMotor = TalonFX(config.driveMotorCANId)
    private val steerMotor = TalonFX(config.steerMotorCANId)

    private val driveFeedForward = SwerveConstants.driveFeedForward.build()

    private val angleOffset = if (Robot.real) config.steerEncoderOffset else config.simulationEncoderOffset

    /* help to optimize turns */
    private var lastAngle: Rotation2d

    /* drive motor control requests */
    private val driveDutyCycle = DutyCycleOut(0.0)
    private val driveVelocity = VelocityVoltage(0.0)

    /* angle motor control requests */
    private val anglePosition = PositionVoltage(0.0)

    init {
        absoluteEncoder.configurator.apply(SwerveCTREConfigurations.swerveCANcoderConfig)

        steerMotor.apply {
            configurator.apply(SwerveCTREConfigurations.swerveSteerFalconConfig)
            resetToAbsolute()
        }

        driveMotor.apply {
            configurator.apply(SwerveCTREConfigurations.swerveDriveKrakenConfig)
            configurator.setPosition(0.0)
        }
        lastAngle = angle
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
        } else {
            driveVelocity.Velocity = MPSToRPS(desiredState.speedMetersPerSecond, WHEEL_CIRCUMFERENCE)
            driveVelocity.FeedForward = driveFeedForward.calculate(desiredState.speedMetersPerSecond)
            driveMotor.setControl(driveVelocity)
        }
    }

    val angle: Rotation2d
        get() = Rotation2d.fromDegrees(falconToDegrees(steerMotor.position.value, SwerveConstants.STEER_GEAR_RATIO))


    /*
    * Returns:
    * Speed of the wheel of the module
    * Angle of the module
    */
    fun getState(): SwerveModuleState {
        return SwerveModuleState(
            /* speed of module */
            rotationsToMeters(driveMotor.velocity.value, WHEEL_CIRCUMFERENCE),
            /* angle of module */
            Rotation2d.fromRotations(steerMotor.position.value)
        )
    }

    fun getPosition(): SwerveModulePosition {
        return SwerveModulePosition(
            /* distance measured by wheel of module */
            rotationsToMeters(driveMotor.position.value, WHEEL_CIRCUMFERENCE),
            /* angle of module */
            Rotation2d.fromRotations(steerMotor.position.value)
        )
    }

    /*
    * Returns: Rotation2d value from position of absolute encoder (degrees)
    */
    private fun getCANcoder(): Rotation2d {
        return Rotation2d.fromDegrees(absoluteEncoder.absolutePosition.value)
    }

    fun resetToAbsolute() {
        val absolutePosition =
            degreesToFalcon(getCANcoder().degrees - config.steerEncoderOffset.degrees, SwerveConstants.STEER_GEAR_RATIO)
        steerMotor.setPosition(absolutePosition)
    }
}