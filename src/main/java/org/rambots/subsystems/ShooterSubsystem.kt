package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.config.ArmConstants
import java.lang.NullPointerException

object ShooterSubsystem : SubsystemBase() {
    private val topMotor= with(CANSparkMax(ArmConstants.TopShooterCANID,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = ArmConstants.ShooterKP
                i = 0.0
                d = ArmConstants.ShooterKD
            }
        }
    }
    private val bottomMotor = with(CANSparkMax(ArmConstants.BottomShooterCANID,CANSparkLowLevel.MotorType.kBrushless)){
        restoreFactoryDefaults()
        setSmartCurrentLimit(4)
        idleMode = CANSparkBase.IdleMode.kBrake
        apply{
            pidController.apply {
                p = ArmConstants.ShooterKP
                i = 0.0
                d = ArmConstants.ShooterKD
            }
        }
    }
    fun shoot(){
        topMotor.pidController.setReference(4000.0,CANSparkBase.ControlType.kVelocity)
        bottomMotor.pidController.setReference(4000.0,CANSparkBase.ControlType.kVelocity)
    }
    fun shoot(topVelocity: Double, bottomVelocity: Double): Command = runOnce {
        topMotor.pidController.setReference(topVelocity, CANSparkBase.ControlType.kVelocity)
        bottomMotor.pidController.setReference(bottomVelocity, CANSparkBase.ControlType.kVelocity)
    }

    override fun periodic() {
        Logger.recordOutput("Top Shooter/Velocity", topMotor.encoder.velocity)
        Logger.recordOutput("Bottom Shooter/Velocity", bottomMotor.encoder.velocity)
    }
}