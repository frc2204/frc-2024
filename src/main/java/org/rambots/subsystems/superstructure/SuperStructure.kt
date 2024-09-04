package org.rambots.subsystems.superstructure

import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.Commands.waitUntil
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import org.rambots.commands.superstructure.primitive.ArmPositionCommand
import org.rambots.commands.superstructure.primitive.ElevatorPositionCommand
import org.rambots.commands.superstructure.primitive.WristPositionCommand

object SuperStructure {

    val homeCommandFromIntake
        get() = SequentialCommandGroup(
            Commands.runOnce({
                ShooterSubsystem.stopIntake()
            }, ShooterSubsystem),
            WristPositionCommand({ 0.0 }, { it > -55.0 }),
            ElevatorPositionCommand({ 0.0 }, { it > -5.0 })
        )

    val intakeCommand
        get() = SequentialCommandGroup(
            ArmPositionCommand({ 0.0 }, { it > -5.0 }),
            ElevatorPositionCommand({ -48.0 }, { it < -30.0 }),
            WristPositionCommand({ -125.0 }, { true }),
            Commands.runOnce({
                ShooterSubsystem.intake()
            }, ShooterSubsystem),
            WaitCommand(0.5),
            waitUntil { ShooterSubsystem.intakeStalling },
            homeCommandFromIntake
        )

    val ampCommand
        get() = SequentialCommandGroup(
            ArmPositionCommand({ -50.0 }, { it < -35.0 }),
            ElevatorPositionCommand({ -68.0 }, { it < -5.0 }),
            WristPositionCommand({ -165.0 }, { it < -155.0 }),
            Commands.runOnce({ ShooterSubsystem.reverseIntake() }, ShooterSubsystem)
        )

    val ampHomingCommand
        get() = SequentialCommandGroup(
            Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem),
            WristPositionCommand({ 0.0 }, { it > -10.0 }),
            ElevatorPositionCommand({ 0.0 }, { it > -10.0 }),
            ArmPositionCommand({ 0.0 }, { true }),
        )

    //    val manualShoot get() = SequentialCommandGroup(
//        ArmPositionCommand ({ 0.0 }, { it < 5.0 }),
//        WristPositionCommand({ -25.0 }, { it < -20.0 }),
//        ShooterCommand({2000.0}, {1500.0}, {topVelocity, bottomVelocity -> ShooterSubsystem.topVelocity >= topVelocity && ShooterSubsystem.bottomVelocity >= bottomVelocity}),
//        Commands.runOnce({ ShooterSubsystem.intake() }, ShooterSubsystem),
//        Commands.runOnce({ ShooterSubsystem.stopShooter() }, ShooterSubsystem),
//        Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem),
//        homeCommand
//    )
    val climb
        get() = SequentialCommandGroup(
            ArmPositionCommand({ -70.0 }, { it < -50 }),
            ElevatorPositionCommand({ -70.0 }, { it < -60 }),
            WaitCommand(1.0),
            ElevatorPositionCommand { 0.0 }
        )

}