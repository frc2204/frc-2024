package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.superstructure.ShooterSubsystem

class AmpScoreCommandGroup : SequentialCommandGroup(
    ArmPositionCommand( { 50.0 }, { it > 35.0}),
    ElevatorPositionCommand({ 68.0 }, { it > 5.0}),
    WristPositionCommand({ 165.0 }, { it > 155.0 }),
    Commands.runOnce({ ShooterSubsystem.reverseIntake() }, ShooterSubsystem)
)
