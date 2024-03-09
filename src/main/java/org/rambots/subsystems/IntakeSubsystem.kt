package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.ArmConstants

object IntakeSubsystem : SubsystemBase() {
    private val topMotor = with(CANSparkMax(ArmConstants.IntakeMotorCANID,CANSparkLowLevel.MotorType.kBrushless)){
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
    private val bottomMotor = with(CANSparkMax(18,CANSparkLowLevel.MotorType.kBrushless)){
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

    fun intakeRoll(): Command = runOnce{
        topMotor.set(ArmConstants.IntakePower)
        bottomMotor.set(ArmConstants.IntakePower)
    }
    fun feed(topPower: Double, bottomPower:Double){
        topMotor.set(topPower)
        bottomMotor.set(bottomPower)
    }
}