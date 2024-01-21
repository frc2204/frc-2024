package org.rambots.commands

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.filter.SlewRateLimiter
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj2.command.Command
import org.rambots.config.SwerveConstants.Controls.DRIVE_STICK_SLEW_RATE
import org.rambots.config.SwerveConstants.Controls.ROTATION_STICK_SLEW_RATE
import org.rambots.config.SwerveConstants.Controls.STICK_DEADBAND
import org.rambots.config.SwerveConstants.MAX_OMEGA
import org.rambots.config.SwerveConstants.MAX_SPEED
import org.rambots.subsystems.SwerveSubsystem

class SwerveTeleop(
    /* values passed in as methods */
    private val translation: () -> Double,
    private val strafe: () -> Double,
    private val rotation: () -> Double,
    private val robotCentric: () -> Boolean,
) : Command() {
    init {
        addRequirements(SwerveSubsystem)
    }

    /* slew rate limiters for x, y, and rotational controls (limits sudden rate of change) */
    private val translationSlewRateLimiter = SlewRateLimiter(DRIVE_STICK_SLEW_RATE)
    private val strafeSlewRateLimiter = SlewRateLimiter(DRIVE_STICK_SLEW_RATE)
    private val rotationalSlewRateLimiter = SlewRateLimiter(ROTATION_STICK_SLEW_RATE)

    override fun execute() {
        /* filtering inputs using the slew rate limiters */
        val translationSlew = translationSlewRateLimiter.calculate(translation())
        val strafeSlew = strafeSlewRateLimiter.calculate(strafe())
        val rotationSlew = rotationalSlewRateLimiter.calculate(rotation())

        /* applying dead band to filtered input */
        val translationValue = MathUtil.applyDeadband(translationSlew, STICK_DEADBAND)
        val strafeValue = MathUtil.applyDeadband(strafeSlew, STICK_DEADBAND)
        val rotationValue = MathUtil.applyDeadband(rotationSlew, STICK_DEADBAND)

        SwerveSubsystem.drive(
            Translation2d(translationValue, strafeValue).times(MAX_SPEED),
            rotationValue * MAX_OMEGA.radians,
            !robotCentric(),
            true
        )
    }
}