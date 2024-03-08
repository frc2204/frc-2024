package org.rambots.subsystems

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand

object SuperstructureSubsystem {
    fun intake() {
        SequentialCommandGroup(
            ElevatorSubsystem.elevatorIntakePosition(),
            ArmSubsystem.armIntakePosition(),
            WristSubsystem.wristIntakePosition()
        )
    }

    fun climb() {
        SequentialCommandGroup(
            ElevatorSubsystem.extend(),
            WaitCommand(1.0),
            ElevatorSubsystem.retract()
        ).schedule()
    }
}