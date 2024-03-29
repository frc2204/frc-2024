package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger
import org.rambots.config.ArmConstants.ARM_NEO_FOLLOWER_ID
import org.rambots.config.ArmConstants.ARM_NEO_LEADER_ID
import org.rambots.config.ArmConstants.ARM_PID

object ArmSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0 + offset
    var position
        get() = leader.encoder.position
        set(value) {
            leader.pidController.setReference(value, CANSparkBase.ControlType.kPosition)
        }

    private val leader = CANSparkMax(ARM_NEO_LEADER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()
        
        idleMode = CANSparkBase.IdleMode.kBrake
        pidController.apply {
            p = ARM_PID.kP
            i = ARM_PID.kI
            d = ARM_PID.kD
        }

        setSmartCurrentLimit(40)
    }

    private val follower = CANSparkMax(ARM_NEO_FOLLOWER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        idleMode = CANSparkBase.IdleMode.kBrake
        setSmartCurrentLimit(40)
    }

    init {
        follower.follow(leader, true)
    }

    override fun periodic() {
        Logger.recordOutput("Arm/DesiredPosition", desiredPosition)
        Logger.recordOutput("Arm/Offset", offset)
        Logger.recordOutput("Arm/Position", leader.encoder.position)
        Logger.recordOutput("Arm/Velocity", leader.encoder.velocity)
        Logger.recordOutput("Arm/Current", leader.outputCurrent)
    }

}