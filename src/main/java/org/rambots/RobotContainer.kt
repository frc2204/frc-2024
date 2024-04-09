// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
package org.rambots

import com.fasterxml.jackson.databind.util.Named
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.geometry.*
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser
import org.rambots.commands.*
import org.rambots.commands.superstructure.actions.*
import org.rambots.commands.superstructure.primitive.*
import org.rambots.commands.superstructure.actions.AutoShootCommand
import org.rambots.subsystems.*
import org.rambots.subsystems.drive.*
import org.rambots.subsystems.drive.DriveConstants.moduleConfigs
import org.rambots.subsystems.superstructure.*
import org.rambots.subsystems.vision.AprilTagVision
import org.rambots.subsystems.vision.AprilTagVisionIO
import org.rambots.subsystems.vision.AprilTagVisionIOLimelight
import org.rambots.subsystems.vision.AprilTagVisionIOPhotonVisionSIM
import org.rambots.util.VisionHelpers.TimestampedVisionUpdate
import kotlin.math.pow


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
object RobotContainer {
    // Subsystems
    var drive: Drive
    val driveController = DriveController()

    private var aprilTagVision: AprilTagVision

    // Controller
    val controller = CommandPS5Controller(0)
    val xbox = CommandXboxController(1)

    // Dashboard inputs
    private val autoChooser: LoggedDashboardChooser<Command>

    val hasLocalized get() = aprilTagVision.hasSeenTarget()
    val hasActiveTag get() = aprilTagVision.isTargetVisible

    //   private final LoggedTunableNumber flywheelSpeedInput = new LoggedTunableNumber("Flywheel Speed", 1500.0);
    /** The container for the robot. Contains subsystems, OI devices, and commands.  */
    init {

        LightingSubsystem

        when (Constants.getMode()) {
            Constants.Mode.REAL -> {
                // Real robot, instantiate hardware IO implementations
                drive = Drive(
                    GyroIO_ADIS(),
                    ModuleIOTalonFX(moduleConfigs[0]),
                    ModuleIOTalonFX(moduleConfigs[1]),
                    ModuleIOTalonFX(moduleConfigs[2]),
                    ModuleIOTalonFX(moduleConfigs[3])
                )

                aprilTagVision = AprilTagVision(AprilTagVisionIOLimelight("limelight-three", drive))
            }

            Constants.Mode.SIM -> {
                // Sim robot, instantiate physics sim IO implementations
                drive =
                    Drive(
                        object : GyroIO {},
                        ModuleIOSim(),
                        ModuleIOSim(),
                        ModuleIOSim(),
                        ModuleIOSim()
                    )
                aprilTagVision =
                    AprilTagVision(
                        AprilTagVisionIOPhotonVisionSIM(
                            "photonCamera1",
                            Transform3d(Translation3d(0.5, 0.0, 0.5), Rotation3d(0.0, 0.0, 0.0))
                        ) { drive.drive })
            }

            else -> {
                // Replayed robot, disable IO implementations
                drive =
                    Drive(
                        object : GyroIO {},
                        object : ModuleIO {},
                        object : ModuleIO {},
                        object : ModuleIO {},
                        object : ModuleIO {})
                aprilTagVision = AprilTagVision(object : AprilTagVisionIO {})
            }
        }

        NamedCommands.registerCommand("Intake", IntakeCommandGroup())
        NamedCommands.registerCommand("IntakeHome", IntakeHomeCommandGroup())
        NamedCommands.registerCommand("Stop", StopShooterCommand())
        NamedCommands.registerCommand("Shoot", AutoShootCommand(driveController) { drive.pose })
        NamedCommands.registerCommand("ConfirmShoot", IntakeCommand())
//
//        // Registering named commands
//        NamedCommands.registerCommand("aim", AutoAimCommand(driveController) {drive.pose} )
//        NamedCommands.registerCommand("startShooter", Commands.runOnce({ ShooterSubsystem.shoot() }, ShooterSubsystem))
//        NamedCommands.registerCommand("intakeToShooter", Commands.runOnce({ ShooterSubsystem.intake()}, ShooterSubsystem))
//        NamedCommands.registerCommand("stopShooter", Commands.runOnce({ ShooterSubsystem.stopShooter()}, ShooterSubsystem))
//        NamedCommands.registerCommand("stopIntake", Commands.runOnce({ ShooterSubsystem.stopIntake()}, ShooterSubsystem))
//        NamedCommands.registerCommand("groundIntake", SuperStructure.intakeCommand)
//        NamedCommands.registerCommand("homeFromIntake", SuperStructure.homeCommandFromIntake)
//        NamedCommands.registerCommand("ampScore", SuperStructure.ampCommand)
//        NamedCommands.registerCommand("homeFromAmp", SuperStructure.ampHomingCommand)
//        NamedCommands.registerCommand("closeToSpeakerWristPosition", Commands.runOnce({ WristPositionCommand({-75.0}, {it < -70.0}) }))

        // Set up auto routines
        // NamedCommands.registerCommand(
        //     "Run Flywheel",
        //     Commands.startEnd(
        //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel)
        //         .withTimeout(5.0));
        autoChooser = LoggedDashboardChooser("Auto Choices", AutoBuilder.buildAutoChooser())

        // Set up SysId routines
        autoChooser.addOption("Drive SysId (Quasistatic Forward)", drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward))
        autoChooser.addOption(
            "Drive SysId (Quasistatic Reverse)",
            drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse)
        )
        autoChooser.addOption(
            "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward)
        )
        autoChooser.addOption(
            "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse)
        )

        // autoChooser.addOption(
        //     "Flywheel SysId (Quasistatic Forward)",
        //     flywheel.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
        // autoChooser.addOption(
        //     "Flywheel SysId (Quasistatic Reverse)",
        //     flywheel.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
        // autoChooser.addOption(
        //     "Flywheel SysId (Dynamic Forward)",
        // flywheel.sysIdDynamic(SysIdRoutine.Direction.kForward));
        // autoChooser.addOption(
        //     "Flywheel SysId (Dynamic Reverse)",
        // flywheel.sysIdDynamic(SysIdRoutine.Direction.kReverse));

        // Configure the button bindings
        aprilTagVision.setDataInterfaces { visionData: List<TimestampedVisionUpdate?>? ->
            drive.addVisionData(visionData)
        }

        driveController.setPoseSupplier { drive.pose }
        driveController.disableHeadingControl()
        configureButtonBindings()
    }

    fun unlockAllMotors() {
        drive.setBrakeMode(false)

        ArmSubsystem.setBrakeMode(false)
    }

    fun lockAllMotors() {
        drive.setBrakeMode(true)

        ArmSubsystem.setBrakeMode(true)
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {
        drive.defaultCommand = DriveCommands.joystickDrive(
            drive,
            driveController,
            { (-controller.leftY).pow(1.0) },
            { (-controller.leftX).pow(1.0) },
            { (-controller.rightX).pow(1.0) }
        )

        controller.L1().onTrue(AmpScoreCommandGroup())
        controller.L1().onFalse(AmpScoreHomeCommandGroup())

        controller.L2().onTrue(IntakeCommandGroup())
        controller.L2().onFalse(IntakeHomeCommandGroup())

        controller.R1().onTrue(BackAmpCommandGroup())
        controller.R2().whileTrue(AutoShootCommand(driveController) { drive.pose })

        controller.square().whileTrue(IntakeCommand())
        controller.triangle().whileTrue(ReverseIntakeCommand())
        controller.circle().whileTrue(ReverseSlowIntakeCommand())

        xbox.povUp().whileTrue(Commands.runOnce({ WristSubsystem.desiredPosition--}))
        xbox.povDown().whileTrue(Commands.runOnce({ WristSubsystem.desiredPosition++}))
        xbox.povLeft().whileTrue(ReverseIntakeCommand())
        xbox.povRight().whileTrue(IntakeCommand())

        xbox.a().onTrue(FullHomeCommandGroup())
        xbox.x().onTrue(ExtendClimberCommandGroup())
        xbox.y().whileTrue(RetractClimberCommand())
        xbox.b().whileTrue(ExtendClimberCommand())

        xbox.rightTrigger().whileTrue(ShootCommand())

//
//        controller.cross().onTrue(SuperStructure.ampCommand)
//        controller.cross().onFalse(SuperStructure.ampHomingCommand)
//
//        controller.L2().onTrue(SuperStructure.intakeCommand)
//        controller.L2().onFalse(SuperStructure.homeCommandFromIntake)
//
//        controller.square().onTrue(Commands.runOnce({ ShooterSubsystem.intake() }, ShooterSubsystem))
//        controller.square().onFalse(Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem))
//
//        controller.triangle().onTrue(Commands.runOnce({ ShooterSubsystem.reverseIntake() }, ShooterSubsystem))
//        controller.triangle().onFalse(Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem))
//
//        controller.R1().whileTrue(AutoAimCommand(driveController) { drive.pose })
//
//
//        controller.povLeft().onTrue(Commands.runOnce({ ShooterSubsystem.shoot() }, ShooterSubsystem))
//        controller.povLeft().onFalse(Commands.runOnce({ ShooterSubsystem.stopShooter()}, ShooterSubsystem))
//
//
//        xbox.povUp().onTrue(Commands.runOnce({ ShooterSubsystem.intake() }, ShooterSubsystem))
//        xbox.povUp().onFalse(Commands.runOnce({ ShooterSubsystem.stopIntake()}, ShooterSubsystem))
//
//        xbox.povDown().onTrue(Commands.runOnce({ ShooterSubsystem.reverseIntake() }, ShooterSubsystem))
//        xbox.povDown().onFalse(Commands.runOnce({ ShooterSubsystem.stopIntake()}, ShooterSubsystem))
//
//        xbox.povLeft().onTrue(ElevatorPositionCommand { ElevatorSubsystem.desiredPosition + 5 })
//        xbox.povRight().onTrue(ElevatorPositionCommand { ElevatorSubsystem.desiredPosition - 5})
//
//        xbox.leftBumper().onTrue(ArmPositionCommand { -70.0 })
//        xbox.rightBumper().onTrue(ArmPositionCommand { -0.0 })

//        controller.R2().whileTrue(
//            Commands.startEnd(
//                {
//                    ShooterSubsystem.shoot(7000.0, 7000.0)
//                    ShooterSubsystem.intake()
//                },
//                {
//                    ShooterSubsystem.stopShooter()
//                    ShooterSubsystem.stopIntake()
//                },
//                ShooterSubsystem
//            )
//        )

//        controller.a().whileTrue(PathFinderAndFollow(driveController.driveModeType))

//        controller
//            .b()
//            .whileTrue(
//                Commands.startEnd(
//                    { driveController.enableHeadingControl() }, { driveController.disableHeadingControl() })
//            )
//
//        controller
//            .y()
//            .whileTrue(
//                Commands.runOnce(
//                    {
//                        drive.setAutoStartPose(
//                            Pose2d(Translation2d(4.0, 5.0), Rotation2d.fromDegrees(0.0))
//                        )
//                    })
//            )
//        controller
//            .povDown()
//            .whileTrue(
//                DriveToPoint(
//                    drive, Pose2d(Translation2d(2.954, 3.621), Rotation2d.fromRadians(2.617))
//                )
//            )
//
//        controller
//            .povUp()
//            .whileTrue(
//                MultiDistanceShot(
//                    { drive!!.pose }, FieldConstants.Speaker.centerSpeakerOpening, flywheel
//                )
//            )

    }

    val autonomousCommand: Command
        /**
         * Use this to pass the autonomous command to the main [Robot] class.
         *
         * @return the command to run in autonomous
         */
        get() = autoChooser.get()

}
