package org.rambots.subsystems.superstructure

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger
import org.rambots.config.ElevatorConstants.ELEVATOR_FOLLOWER_ID
import org.rambots.config.ElevatorConstants.ELEVATOR_LEADER_ID
import org.rambots.config.ElevatorConstants.ELEVATOR_MAX_HEIGHT
import org.rambots.config.ElevatorConstants.ELEVATOR_MIN_HEIGHT
import org.rambots.config.ElevatorConstants.ELEVATOR_PID
import org.rambots.util.MathUtil
import org.rambots.util.SparkMaxUtil

object ElevatorSubsystem : SubsystemBase() {

    var offset = 0.0
    var desiredPosition = 0.0
        set(value) {
            field = MathUtil.bound(value, ELEVATOR_MIN_HEIGHT, ELEVATOR_MAX_HEIGHT)
        }

    val position get() = leader.encoder.position

    private val leader = CANSparkMax(ELEVATOR_LEADER_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        pidController.apply {
            p = ELEVATOR_PID.kP
            i = ELEVATOR_PID.kI
            d = ELEVATOR_PID.kD
        }

        idleMode = CANSparkBase.IdleMode.kBrake
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
        leader.pidController.setReference(desiredPosition + offset, CANSparkBase.ControlType.kPosition)

        Logger.recordOutput("Elevator/DesiredPosition", desiredPosition)
        Logger.recordOutput("Elevator/Offset", offset)

        Logger.recordOutput("Elevator/Leader/Position", leader.encoder.position)
        Logger.recordOutput("Elevator/Leader/Velocity", leader.encoder.velocity)
        Logger.recordOutput("Elevator/Leader/Current", leader.outputCurrent)

        Logger.recordOutput("Elevator/Follower/Position", follower.encoder.position)
        Logger.recordOutput("Elevator/Follower/Velocity", follower.encoder.velocity)
        Logger.recordOutput("Elevator/Follower/Current", follower.outputCurrent)
    }

    fun setBrakeMode(brake: Boolean) {
        leader.idleMode = SparkMaxUtil.getBrake(brake)
        follower.idleMode = SparkMaxUtil.getBrake(brake)
    }

}