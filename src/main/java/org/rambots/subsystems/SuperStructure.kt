package org.rambots.subsystems

import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.Commands.waitUntil
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.commands.ArmPositionCommand
import org.rambots.commands.ElevatorPositionCommand
import org.rambots.commands.ShooterCommand
import org.rambots.commands.WristPositionCommand

object SuperStructure {

    private val homeCommand get() = SequentialCommandGroup(
        ArmPositionCommand ({ 0.0 }, { it < 5.0 }),
        WristPositionCommand( { 0.0 }, { it < 5.0 }),
        ElevatorPositionCommand ({ 0.0 }, { true })
    )

    val homeCommandFromIntake get() = SequentialCommandGroup(
        Commands.runOnce({ ShooterSubsystem.stopIntake()}, ShooterSubsystem),
        WristPositionCommand({ 0.0 }, { it > -55.0 }),
        ElevatorPositionCommand({ 0.0 }, { it < 5.0 })
    )

    val intakeCommand get()  = SequentialCommandGroup(
        ArmPositionCommand ({ 0.0 }, { it < 5.0 }),
        ElevatorPositionCommand ({ -48.0 }, { it < -30.0 }),
        WristPositionCommand ({ -125.0 }, { true }),
        Commands.runOnce ({ ShooterSubsystem.intake()}, ShooterSubsystem),
        WaitCommand(0.5),
        waitUntil { ShooterSubsystem.intakeStalling },
        homeCommandFromIntake
    )

    val manualShoot get() = SequentialCommandGroup(
        ArmPositionCommand ({ 0.0 }, { it < 5.0 }),
        WristPositionCommand({ -25.0 }, { it < -20.0 }),
        ShooterCommand({2000.0}, {1500.0}, {topVelocity, bottomVelocity -> ShooterSubsystem.topVelocity >= topVelocity && ShooterSubsystem.bottomVelocity >= bottomVelocity}),
        Commands.runOnce({ ShooterSubsystem.intake() }, ShooterSubsystem),
        Commands.runOnce({ ShooterSubsystem.stopShooter() }, ShooterSubsystem),
        Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem),
        homeCommand
    )
}