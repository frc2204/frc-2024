package org.rambots.subsystems

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.WaitCommand

object SuperstructureSubsystem: SubsystemBase() {
    fun intake() {
        SequentialCommandGroup(
            ElevatorSubsystem.elevatorIntakePosition(),
            ArmSubsystem.armIntakePosition(),
            WristSubsystem.wristIntakePosition(),
            IntakeSubsystem.intakeRoll()
        ).schedule()
    }

    fun climb() {
        SequentialCommandGroup(
            ElevatorSubsystem.extend(),
            WaitCommand(1.0),
            ElevatorSubsystem.retract()
        ).schedule()
    }
}