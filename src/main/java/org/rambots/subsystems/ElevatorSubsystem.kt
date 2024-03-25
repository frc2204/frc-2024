package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger
import org.rambots.config.ArmConstants.ARM_NEO_FOLLOWER_ID
import org.rambots.config.ArmConstants.ARM_NEO_LEADER_ID
import org.rambots.config.ArmConstants.ARM_PID
import org.rambots.config.ElevatorConstants.ELEVATOR_FOLLOWER_ID
import org.rambots.config.ElevatorConstants.ELEVATOR_LEADER_ID
import org.rambots.config.ElevatorConstants.ELEVATOR_PID

object ElevatorSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0 + offset
    var position
        get() = leader.encoder.position
        set(value) {
            leader.pidController.setReference(value, CANSparkBase.ControlType.kPosition)
        }

    private val leader = CANSparkMax(ELEVATOR_LEADER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()
        
        idleMode = CANSparkBase.IdleMode.kBrake
        pidController.apply {
            p = ELEVATOR_PID.kP
            i = ELEVATOR_PID.kI
            d = ELEVATOR_PID.kD
        }

        setSmartCurrentLimit(40)
    }

    private val follower = CANSparkMax(ELEVATOR_FOLLOWER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        idleMode = CANSparkBase.IdleMode.kBrake
        setSmartCurrentLimit(40)
    }

    init {
        follower.follow(leader, true)
    }


    override fun periodic() {
        Logger.recordOutput("Elevator/DesiredPosition", desiredPosition)
        Logger.recordOutput("Elevator/Offset", offset)
        Logger.recordOutput("Elevator/Leader/Position", leader.encoder.position)
        Logger.recordOutput("Elevator/Leader/Velocity", leader.encoder.velocity)
        Logger.recordOutput("Elevator/Leader/Current", leader.outputCurrent)

        Logger.recordOutput("Elevator/Follower/Position", follower.encoder.position)
        Logger.recordOutput("Elevator/Follower/Velocity", follower.encoder.velocity)
        Logger.recordOutput("Elevator/Follower/Current", follower.outputCurrent)
    }

}