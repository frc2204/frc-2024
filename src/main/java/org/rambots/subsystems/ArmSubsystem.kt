package org.rambots.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase


object ArmSubsystem : SubsystemBase() {
   val ArmMotor: CANSparkMax = CANSparkMax(69,CANSparkLowLevel.MotorType.kBrushless)
   val ArmMotorTwo: CANSparkMax = CANSparkMax(69,CANSparkLowLevel.MotorType.kBrushless)
 init {
     ArmMotorTwo.follow(ArmMotor)
     ArmMotor.restoreFactoryDefaults()
     ArmMotor.setSmartCurrentLimit(1,1,1)
     ArmMotor.setIdleMode(CANSparkBase.IdleMode.kBrake)
 }
    fun up () {
        ArmMotor.setVoltage(0.1)
    }
    fun down(){
        ArmMotor.setVoltage(-0.1)
    }
}
