package org.rambots.subsystems.superstructure

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.DutyCycleEncoder
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.config.WristConstants.WRIST_ID
import org.rambots.config.WristConstants.WRIST_PID

object WristSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0

    val position get() = motor.encoder.position

    private val dutyCycleEncoder = DutyCycleEncoder(0).apply {
        positionOffset = 0.5
    }
    private val absoluteEncoderPosition get() = dutyCycleEncoder.absolutePosition - dutyCycleEncoder.positionOffset
    private val relativeEncoderPosition get() = (position / 75.0) * (15.0 / 36.0)
    private val absoluteMotorPosition get() = absoluteEncoderPosition * 75.0 * (36.0 / 15.0)

    private val motor = CANSparkMax(WRIST_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        pidController.apply {
            p = WRIST_PID.kP
            i = WRIST_PID.kI
            d = WRIST_PID.kD
        }

        inverted = true
        idleMode = CANSparkBase.IdleMode.kBrake
        setSmartCurrentLimit(40)
    }

    fun resetEncoder() {
        motor.encoder.position = absoluteMotorPosition
    }

    override fun periodic() {
        motor.pidController.setReference(desiredPosition + offset, CANSparkBase.ControlType.kPosition)

        Logger.recordOutput("Wrist/DesiredPosition", desiredPosition)
        Logger.recordOutput("Wrist/Offset", offset)
        Logger.recordOutput("Wrist/Position", motor.encoder.position)
        Logger.recordOutput("Wrist/PositionThroughEncoder", absoluteMotorPosition)
        Logger.recordOutput("Wrist/ShaftPosition", relativeEncoderPosition)
        Logger.recordOutput("Wrist/Velocity", motor.encoder.velocity)
        Logger.recordOutput("Wrist/Current", motor.outputCurrent)

        Logger.recordOutput("Wrist/Encoder/Connected", dutyCycleEncoder.isConnected)
        Logger.recordOutput("Wrist/Encoder/Position", absoluteEncoderPosition)
    }

    fun home() {
        desiredPosition = 0.0
    }

}