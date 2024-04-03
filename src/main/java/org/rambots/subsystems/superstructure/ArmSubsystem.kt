package org.rambots.subsystems.superstructure

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.motorcontrol.Spark
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.config.ArmConstants.ARM_NEO_FOLLOWER_ID
import org.rambots.config.ArmConstants.ARM_NEO_LEADER_ID
import org.rambots.config.ArmConstants.ARM_PID
import org.rambots.util.SparkMaxUtil

object ArmSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0

    val currentPosition get() = leader.encoder.position

    private val leader = CANSparkMax(ARM_NEO_LEADER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        pidController.apply {
            p = ARM_PID.kP
            i = ARM_PID.kI
            d = ARM_PID.kD
        }

        idleMode = CANSparkBase.IdleMode.kBrake
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
        leader.pidController.setReference(desiredPosition + offset, CANSparkBase.ControlType.kPosition)

        Logger.recordOutput("Arm/DesiredPosition", desiredPosition)
        Logger.recordOutput("Arm/Offset", offset)

        Logger.recordOutput("Arm/Leader/Position", leader.encoder.position)
        Logger.recordOutput("Arm/Leader/Velocity", leader.encoder.velocity)
        Logger.recordOutput("Arm/Leader/Current", leader.outputCurrent)

        Logger.recordOutput("Arm/Follower/Position", follower.encoder.position)
        Logger.recordOutput("Arm/Follower/Velocity", follower.encoder.velocity)
        Logger.recordOutput("Arm/Follower/Current", follower.outputCurrent)
    }

    fun setBrakeMode(brake: Boolean) {
        leader.idleMode = SparkMaxUtil.getBrake(brake)
        follower.idleMode = SparkMaxUtil.getBrake(brake)
    }

}