package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.rambots.config.ArmConstants

object WristSubsystem : SubsystemBase() {
    private val wristMotor = CANSparkMax(ArmConstants.WristMotorCANID, CANSparkLowLevel.MotorType.kBrushless)
    init{
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
    }
    fun wristIntakePosition(): Command = runOnce {
        wristMotor.pidController.setReference(ArmConstants.WristIntakePosition, CANSparkBase.ControlType.kPosition)
    }
    fun getWristPosition(): Double{
        return wristMotor.encoder.position
    }
    fun setWristPosition(value: Double){
        wristMotor.pidController.setReference(value, CANSparkBase.ControlType.kPosition)
    }
}