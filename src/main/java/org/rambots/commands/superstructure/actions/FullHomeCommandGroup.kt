package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.superstructure.WristSubsystem

class FullHomeCommandGroup : SequentialCommandGroup(
    WristPositionCommand({ 0.0 }, { it < 5.0 }),
    ElevatorPositionCommand({ 0.0 }, { it < 5.0 }),
    ArmPositionCommand({ 0.0 }, { it < 5.0 })
)
