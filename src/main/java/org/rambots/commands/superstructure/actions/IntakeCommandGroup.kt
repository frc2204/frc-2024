package org.rambots.commands.superstructure.actions


import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand
import org.rambots.subsystems.LightingSubsystem
import org.rambots.subsystems.superstructure.*
import org.rambots.util.LimelightHelpers

class IntakeCommandGroup : SequentialCommandGroup(
    ArmPositionCommand ({ 0.0 }, { it < 5.0 }),
    ElevatorPositionCommand ({ 55.0 }, { it > 20.0 }),
    WristPositionCommand ({ 130.0 }, { true }),
    Commands.runOnce ({
        ShooterSubsystem.intake()
    }, ShooterSubsystem),
    WaitCommand(0.5),
    Commands.waitUntil { ShooterSubsystem.intakeStalling },
    Commands.runOnce( {
        LightingSubsystem.blinkLL()
        SequentialCommandGroup(WaitCommand(5.0), Commands.runOnce( {
            LightingSubsystem.clearLL()
        })).schedule()
    }),
    IntakeHomeCommandGroup()
) {

    init {
        addRequirements(ArmSubsystem, ElevatorSubsystem, WristSubsystem, LightingSubsystem)
    }

}
