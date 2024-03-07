package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.ArmConstants

object IntakeSubsystem : SubsystemBase() {
    private val intakeMotor = with(CANSparkMax(ArmConstants.IntakeMotorCANID,CANSparkLowLevel.MotorType.kBrushless)){
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
        intakeMotor.set(ArmConstants.IntakePower)
    }
}