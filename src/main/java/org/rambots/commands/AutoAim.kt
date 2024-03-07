package org.rambots.commands

import edu.wpi.first.wpilibj2.command.Command
import org.rambots.subsystems.ArmSubsystem
import org.rambots.subsystems.SwerveSubsystem
import org.rambots.subsystems.AutoAimSubsystem
import org.rambots.subsystems.ShooterSubsystem

class AutoAim : Command() {
    init {
        addRequirements(SwerveSubsystem,AutoAimSubsystem,ShooterSubsystem)
    }

    override fun initialize() {
        SwerveSubsystem.drive(
            SwerveSubsystem.pose.translation-AutoAimSubsystem.desiredPose.translation,
            AutoAimSubsystem.desiredPose.rotation.radians,
            true, true
        )
        ArmSubsystem.setArmPosition(AutoAimSubsystem.armEncoderValue)
        ArmSubsystem.setWristPosition(AutoAimSubsystem.wristEncoderValue)
    }

    override fun execute() {
        if(SwerveSubsystem.pose==AutoAimSubsystem.desiredPose &&
            ArmSubsystem.getArmPosition()==AutoAimSubsystem.armEncoderValue &&
            ArmSubsystem.getWristPosition()==AutoAimSubsystem.wristEncoderValue
        ) {
            ShooterSubsystem.shoot(
                AutoAimSubsystem.topPower,
                AutoAimSubsystem.bottomPower,
                AutoAimSubsystem.feedPower
            )
        }
    }

    override fun isFinished(): Boolean {
        return false
    }


    override fun end(interrupted: Boolean) {}
}