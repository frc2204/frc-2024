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
                p = 1.0
                i = 0.0
                d = 0.0
            }
        }
    }
    private val bottomMotor = with(CANSparkMax(17,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = 1.0
                i = 0.0
                d = 0.0
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
        topMotor.set(0.5)
        bottomMotor.set(0.5)
        feedMotor.set(0.2)
    }
    fun shoot(topPower:Double,bottomPower:Double,feedPower:Double){
        topMotor.set(topPower)
        bottomMotor.set(bottomPower)
        feedMotor.set(feedPower)
    }
}