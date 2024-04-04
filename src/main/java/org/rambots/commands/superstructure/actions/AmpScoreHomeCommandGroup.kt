package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.superstructure.ShooterSubsystem

class AmpScoreHomeCommandGroup : SequentialCommandGroup(
    Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem),
    WristPositionCommand( { 0.0 }, { it < 70.0 }),
    ElevatorPositionCommand ({ 0.0 }, { it < 30.0 }),
    ArmPositionCommand ({ 0.0 }, { true }),
)
