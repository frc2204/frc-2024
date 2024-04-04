package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.superstructure.ShooterSubsystem

class BackAmpCommandGroup : SequentialCommandGroup(
    ArmPositionCommand { 70.0 },
)
