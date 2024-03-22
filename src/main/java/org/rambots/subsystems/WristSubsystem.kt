package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger
import org.rambots.Robot
import org.rambots.config.ArmConstants.ARM_PID
import org.rambots.config.WristConstants.WRIST_ID
import org.rambots.config.WristConstants.WRIST_PID
import org.rambots.util.AllianceFlipUtil
import org.rambots.util.FieldConstants

object WristSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0 + offset
    var hasBeenHomed = false

    var position
        get() = motor.encoder.position
        set(value) {
            motor.pidController.setReference(value, CANSparkBase.ControlType.kPosition)
        }

    private val motor = CANSparkMax(WRIST_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()
        
        idleMode = CANSparkBase.IdleMode.kBrake
        pidController.apply {
            p = WRIST_PID.kP
            i = WRIST_PID.kI
            d = WRIST_PID.kD
        }

        setSmartCurrentLimit(40)
    }

    private fun stop() {
        motor.stopMotor()
    }

    override fun periodic() {
        Logger.recordOutput("Wrist/DesiredPosition", desiredPosition)
        Logger.recordOutput("Wrist/Offset", offset)
        Logger.recordOutput("Wrist/Position", motor.encoder.position)
        Logger.recordOutput("Wrist/Velocity", motor.encoder.velocity)
        Logger.recordOutput("Wrist/Current", motor.outputCurrent)
    }

}