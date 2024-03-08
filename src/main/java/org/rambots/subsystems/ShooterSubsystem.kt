package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import java.lang.NullPointerException

object ShooterSubsystem : SubsystemBase() {
    private val topMotor= with(CANSparkMax(16,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = 0.00035
                i = 0.0
                d = 0.15
            }
        }
    }
    private val bottomMotor = with(CANSparkMax(17,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = 0.00035
                i = 0.0
                d = 0.15
            }
        }
    }
    private val feedMotor = with(CANSparkMax(18,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = 0.0
                i = 0.0
                d = 0.0
            }
        }
    }
    fun shoot(){
        topMotor.pidController.setReference(4000.0,CANSparkBase.ControlType.kVelocity)
        bottomMotor.pidController.setReference(4000.0,CANSparkBase.ControlType.kVelocity)
        feedMotor.pidController.setReference(4000.0, CANSparkBase.ControlType.kVelocity)
    }
    fun shoot(topPower:Double,bottomPower:Double,feedPower:Double){
        topMotor.pidController.setReference(topPower,CANSparkBase.ControlType.kVelocity)
        bottomMotor.pidController.setReference(bottomPower,CANSparkBase.ControlType.kVelocity)
        feedMotor.pidController.setReference(feedPower,CANSparkBase.ControlType.kVelocity)
    }
}