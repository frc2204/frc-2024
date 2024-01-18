package org.rambots

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.button.Trigger
import org.rambots.commands.SwerveTeleop
import org.rambots.config.SwerveConstants.Controls.DRIVE_BABY_POWER
import org.rambots.config.SwerveConstants.Controls.DRIVE_POWER
import org.rambots.config.SwerveConstants.Controls.ROTATIONAL_POWER
import org.rambots.subsystems.SwerveSubsystem

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 *
 * In Kotlin, it is recommended that all your Subsystems are Kotlin objects. As such, there
 * can only ever be a single instance. This eliminates the need to create reference variables
 * to the various subsystems in this container to pass into to commands. The commands can just
 * directly reference the (single instance of the) object.
 */
object RobotContainer {

    private val primaryJoystick = Joystick(0)
    private val secondaryJoystick = Joystick(1)
    private val xboxController = XboxController(2)
    init {
        configureBindings()

        /* sets swerve subsystem's default command as swerve teleop */
        SwerveSubsystem.defaultCommand = SwerveTeleop(
            { primaryJoystick.y * DRIVE_POWER  + xboxController.getRawAxis(1) * DRIVE_BABY_POWER},
            { primaryJoystick.x * DRIVE_POWER  + xboxController.getRawAxis(0) * DRIVE_BABY_POWER},
            { secondaryJoystick.x * ROTATIONAL_POWER },
            { false }
        )
    }

    /** Use this method to define your `trigger->command` mappings. */
    private fun configureBindings() {
        Trigger { secondaryJoystick.trigger }.onTrue(SwerveSubsystem.zeroGyro())
    }

    fun getAutonomousCommand(): Command? {
        return null
    }
}