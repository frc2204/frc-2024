package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.rambots.commands.superstructure.primitive.ArmPositionCommand

class BackAmpCommandGroup : SequentialCommandGroup(
    ArmPositionCommand { 70.0 },
)
