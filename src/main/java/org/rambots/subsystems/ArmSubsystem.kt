package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkBase.ControlType
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.rambots.config.ArmConstants


object ArmSubsystem : SubsystemBase() {
    /* motor initialization: neos and cansparkmax */
    private val motorOne = CANSparkMax(ArmConstants.ArmMotorOneCANID, CANSparkLowLevel.MotorType.kBrushless)
    private val motorTwo = CANSparkMax(ArmConstants.ArmMotorTwoCANID, CANSparkLowLevel.MotorType.kBrushless)
    private val wristMotor = CANSparkMax(ArmConstants.WristMotorCANID, CANSparkLowLevel.MotorType.kBrushless)

    init {
        motorOne.apply {
            restoreFactoryDefaults()
            setSmartCurrentLimit(10)
            idleMode = CANSparkBase.IdleMode.kBrake

            pidController.apply {
                p = ArmConstants.ArmKP
                i = ArmConstants.ArmKI
                d = ArmConstants.ArmKD
            }
        }
        motorTwo.apply {
            restoreFactoryDefaults()
            setSmartCurrentLimit(10)
            idleMode = CANSparkBase.IdleMode.kBrake

            pidController.apply {
                p = ArmConstants.ArmKP
                i = ArmConstants.ArmKI
                d = ArmConstants.ArmKD
            }
        }
        wristMotor.apply {
            restoreFactoryDefaults()
            setSmartCurrentLimit(5)
            idleMode = CANSparkBase.IdleMode.kBrake

            pidController.apply {
                p = ArmConstants.WristKP
                i = ArmConstants.WristKI
                d = ArmConstants.WristKD
            }
        }
        motorTwo.follow(motorOne)
    }

    override fun periodic() {
        /* debug */
        SmartDashboard.putNumber("Arm Position", motorOne.encoder.position)
//        SmartDashboard.putNumber("Wrist Position", wristMotor.encoder.position)
    }

    /* moves arm to intake position */
    fun armIntakePosition(): Command = runOnce {
        motorOne.pidController.setReference(ArmConstants.ArmIntakePosition, CANSparkBase.ControlType.kPosition)
    }

    /* moves wrist to intake position */
    fun wristIntakePosition(): Command = runOnce {
        wristMotor.pidController.setReference(ArmConstants.WristIntakePosition, CANSparkBase.ControlType.kPosition)
    }

    fun armClimbPosition(): Command = runOnce {
        motorOne.pidController.setReference(ArmConstants.ArmClimbPosition, CANSparkBase.ControlType.kPosition)
    }

    fun armHomePosition(): Command = runOnce {
        motorOne.pidController.setReference(ArmConstants.ArmIntakePosition, CANSparkBase.ControlType.kPosition)
    }

    fun getArmPosition(): Double {
        return motorOne.encoder.position
    }
    fun getWristPosition(): Double{
        return wristMotor.encoder.position
    }

    fun setArmPosition(value: Double) {
        motorOne.pidController.setReference(value,ControlType.kPosition)
    }
    fun setWristPosition(value: Double){
        wristMotor.pidController.setReference(value,ControlType.kPosition)
    }

    private fun resetEncoder() {
        motorOne.encoder.position = 0.0
    }
}