package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.config.ElevatorConstants
import org.rambots.subsystems.superstructure.ElevatorSubsystem

class ExtendClimberCommandGroup : SequentialCommandGroup(
    WristPositionCommand { 0.0 },
    ElevatorPositionCommand({ 0.0 }, { it < 5.0 }),
    ArmPositionCommand({ 80.0 }, { it > 75.0 }),
    ElevatorPositionCommand({ ElevatorConstants.ELEVATOR_MAX_HEIGHT }, { it > ElevatorConstants.ELEVATOR_MAX_HEIGHT - 5.0 })
)
