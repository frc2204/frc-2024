package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.rambots.config.ArmConstants

object ElevatorSubsystem : SubsystemBase() {
    private val table = NetworkTableInstance.getDefault().getTable("")
    val x : NetworkTableEntry = table.getEntry("ty")

    private val motorOne = with(CANSparkMax(ArmConstants.ElevatorMotorOneCANID, CANSparkLowLevel.MotorType.kBrushless)) {
        restoreFactoryDefaults()
        setSmartCurrentLimit(5)
        idleMode = CANSparkBase.IdleMode.kBrake
        inverted = true
        apply {
            pidController.apply {
                p = ArmConstants.ElevatorKP
                i = ArmConstants.ElevatorKI
                d = ArmConstants.ElevatorKD
            }
        }
    }
    private val motorTwo = with(CANSparkMax(ArmConstants.ElevatorMotorTwoCANID, CANSparkLowLevel.MotorType.kBrushless)) {
        restoreFactoryDefaults()
        setSmartCurrentLimit(5)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply {
            pidController.apply {
                p = ArmConstants.ElevatorKP
                i = ArmConstants.ElevatorKI
                d = ArmConstants.ElevatorKD
            }
        }
    }

    init {
        motorTwo.follow(motorOne, true)
    }

    private val position
        get() = motorOne.encoder.position

    fun extend(): Command = runOnce{
        motorOne.pidController.setReference(ArmConstants.ElevatorClimbPosition,CANSparkBase.ControlType.kPosition)
    }
    fun retract(): Command = runOnce{
        motorOne.pidController.setReference(ArmConstants.ElevatorRetractPosition,CANSparkBase.ControlType.kPosition)
    }
    fun elevatorIntakePosition(): Command = runOnce{
        motorOne.pidController.setReference(ArmConstants.ElevatorIntakePosition,CANSparkBase.ControlType.kPosition)
    }
    override fun periodic() {
        SmartDashboard.putNumber("Position", position)
    }
}