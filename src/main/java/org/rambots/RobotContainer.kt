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

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.math.geometry.*
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser
import org.rambots.commands.*
import org.rambots.subsystems.*
import org.rambots.subsystems.drive.*
import org.rambots.subsystems.drive.DriveConstants.moduleConfigs
import org.rambots.subsystems.vision.AprilTagVision
import org.rambots.subsystems.vision.AprilTagVisionIO
import org.rambots.subsystems.vision.AprilTagVisionIOLimelight
import org.rambots.subsystems.vision.AprilTagVisionIOPhotonVisionSIM
import org.rambots.util.VisionHelpers.TimestampedVisionUpdate


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
object RobotContainer {
    // Subsystems
    public var drive: Drive
    private var aprilTagVision: AprilTagVision
    public val driveController = DriveController()

    // Controller
    private val controller = CommandPS5Controller(0)

    // Dashboard inputs
    private val autoChooser: LoggedDashboardChooser<Command>

    val hasLocalized get() = aprilTagVision.hasSeenTarget()
    val hasActiveTag get() = aprilTagVision.isTargetVisible

    //   private final LoggedTunableNumber flywheelSpeedInput =
    //       new LoggedTunableNumber("Flywheel Speed", 1500.0);
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

                // flywheel = new Flywheel(new FlywheelIOSparkMax());
                // drive = new Drive(
                // new GyroIOPigeon2(true),
                // new ModuleIOTalonFX(0),
                // new ModuleIOTalonFX(1),
                // new ModuleIOTalonFX(2),
                // new ModuleIOTalonFX(3));
//                flywheel = Flywheel(FlywheelIOTalonFX())
                aprilTagVision = AprilTagVision(AprilTagVisionIOLimelight("limelight-three"))
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
//                flywheel = Flywheel(FlywheelIOSim())
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
//                flywheel = Flywheel(object : FlywheelIO {})
                aprilTagVision = AprilTagVision(object : AprilTagVisionIO {})
            }
        }
        // Set up auto routines
        // NamedCommands.registerCommand(
        //     "Run Flywheel",
        //     Commands.startEnd(
        //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel)
        //         .withTimeout(5.0));
        autoChooser = LoggedDashboardChooser("Auto Choices", AutoBuilder.buildAutoChooser())

        // Set up SysId routines
        autoChooser.addOption(
            "Drive SysId (Quasistatic Forward)",
            drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward)
        )
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

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {
        drive.defaultCommand = DriveCommands.joystickDrive(
            drive,
            driveController,
            { -controller.leftY },
            { -controller.leftX },
            { -controller.rightX }
        )
        ArmSubsystem.defaultCommand = ArmPositionCommand { ArmSubsystem.desiredPosition }
        ElevatorSubsystem.defaultCommand = ElevatorPositionCommand { ElevatorSubsystem.desiredPosition }
        WristSubsystem.defaultCommand = WristPositionCommand { WristSubsystem.desiredPosition }


//        controller.L1().whileTrue(AmpScoring())
//        controller.R2().whileTrue(SourceIntake())
//
//        controller.square().whileTrue(ArmPositionCommand { -70.0 })
//        controller.circle().whileTrue(ArmPositionCommand { 0.0 })

//        controller.povUp().whileTrue(ElevatorPositionCommand { 0.0 })
//        controller.povDown().whileTrue(ElevatorPositionCommand { -48.0 })
//
//        controller.povLeft().whileTrue(WristPositionCommand { -125.0 })
//        controller.povRight().whileTrue(WristPositionCommand { 0.0 })

//        controller.cross().whileTrue(Commands.startEnd({ ShooterSubsystem.intake() }, { ShooterSubsystem.stopIntake()}, ShooterSubsystem ))
//        controller.circle().whileTrue(Commands.startEnd({ShooterSubsystem.shoot(3000.0, 3000.0)}, {ShooterSubsystem.stopShooter()}, ShooterSubsystem))

        controller.L2().onTrue(SuperStructure.intakeCommand)
        controller.L2().onFalse(SuperStructure.homeCommandFromIntake)

        controller.circle().onTrue(Commands.runOnce({ ShooterSubsystem.shoot(6000.0, 6000.0) }, ShooterSubsystem))
        controller.circle().onFalse(Commands.runOnce({ ShooterSubsystem.stopShooter() }, ShooterSubsystem))

        controller.square().onTrue(Commands.runOnce({ ShooterSubsystem.intake() }, ShooterSubsystem))
        controller.square().onFalse(Commands.runOnce({ ShooterSubsystem.stopIntake() }, ShooterSubsystem))

        controller.povUp().whileTrue(Commands.runOnce({ WristSubsystem.desiredPosition++}))
        controller.povDown().whileTrue(Commands.runOnce({ WristSubsystem.desiredPosition--}))


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

        // controller
        //     .b()
        //     .onTrue(
        //         Commands.runOnce(
        //                 () ->
        //                     drive.setPose(
        //                         new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
        //                 drive)
        //             .ignoringDisable(true));
        // controller
        //     .a()
        //     .whileTrue(
        //         Commands.startEnd(
        //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel));
    }

    val autonomousCommand: Command
        /**
         * Use this to pass the autonomous command to the main [Robot] class.
         *
         * @return the command to run in autonomous
         */
        get() = autoChooser.get()

}
