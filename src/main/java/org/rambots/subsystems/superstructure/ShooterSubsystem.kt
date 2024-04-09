package org.rambots.subsystems.superstructure

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger
import org.rambots.RobotContainer
import org.rambots.config.ShooterConstants.INTAKE_BOTTOM_ID
import org.rambots.config.ShooterConstants.INTAKE_OUTPUT
import org.rambots.config.ShooterConstants.INTAKE_TOP_ID
import org.rambots.config.ShooterConstants.SHOOTER_BOTTOM_ID
import org.rambots.config.ShooterConstants.SHOOTER_TOP_ID
import org.rambots.config.ShooterConstants.INTAKE_SPIKE_LIMIT
import java.awt.Robot
import kotlin.math.abs

object ShooterSubsystem : SubsystemBase() {

    var topDesiredVelocity = 0.0
    var bottomDesiredVelocity = 0.0

    val topVelocity get() = shooterTop.encoder.velocity
    val bottomVelocity get() = shooterBottom.encoder.velocity

    val shooterTop = CANSparkMax(SHOOTER_TOP_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        idleMode = CANSparkBase.IdleMode.kCoast

    }

    val shooterBottom = CANSparkMax(SHOOTER_BOTTOM_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()

        idleMode = CANSparkBase.IdleMode.kCoast

    }

    private val intakeTopLead = CANSparkMax(INTAKE_TOP_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()
        idleMode = CANSparkBase.IdleMode.kBrake
    }
    private val intakeBottomFollower = CANSparkMax(INTAKE_BOTTOM_ID, CANSparkLowLevel.MotorType.kBrushless).apply {
        restoreFactoryDefaults()
        idleMode = CANSparkBase.IdleMode.kBrake
    }

    private val intakeCurrent get() = intakeTopLead.outputCurrent

    val intakeStalling get() = intakeCurrent > INTAKE_SPIKE_LIMIT

    init {
        intakeBottomFollower.follow(intakeTopLead, false)
    }

    fun intake() {
        intakeTopLead.set(INTAKE_OUTPUT)
    }

    fun reverseIntake() {
        intakeTopLead.set(-INTAKE_OUTPUT)
    }

    fun reverseIntakeSlow() {
        intakeTopLead.set(-INTAKE_OUTPUT * 0.5)
    }

    fun stopIntake() {
        intakeTopLead.stopMotor()
    }

    fun shoot() {
        shooterTop.set(-1.0)
        shooterBottom.set(-1.0)
    }

    fun reverseShooter() {
        shooterTop.set(INTAKE_OUTPUT)
        shooterBottom.set(INTAKE_OUTPUT)
    }

    fun stopShooter() {
        shooterTop.set(0.0)
        shooterBottom.set(0.0)
    }

    override fun periodic() {

        if (abs(topVelocity) > 5000.0 && abs(bottomVelocity) > 5000.0) {
            RobotContainer.controller.hid.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
            RobotContainer.xbox.hid.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
        } else {
            RobotContainer.controller.hid.setRumble(GenericHID.RumbleType.kBothRumble, 0.0)
            RobotContainer.xbox.hid.setRumble(GenericHID.RumbleType.kBothRumble, 0.0)
        }

        Logger.recordOutput("Shooter/Intake/Top/Output", intakeTopLead.appliedOutput)
        Logger.recordOutput("Shooter/Intake/Top/Velocity", intakeTopLead.encoder.velocity)
        Logger.recordOutput("Shooter/Intake/Top/Current", intakeTopLead.outputCurrent)
        Logger.recordOutput("Shooter/Intake/Top/Temperature", intakeTopLead.motorTemperature)

        Logger.recordOutput("Shooter/Intake/Bottom/Output", intakeBottomFollower.appliedOutput)
        Logger.recordOutput("Shooter/Intake/Bottom/Velocity", intakeBottomFollower.encoder.velocity)
        Logger.recordOutput("Shooter/Intake/Bottom/Current", intakeBottomFollower.outputCurrent)
        Logger.recordOutput("Shooter/Intake/Bottom/Temperature", intakeBottomFollower.motorTemperature)

        Logger.recordOutput("Shooter/Top/Output", shooterTop.appliedOutput)
        Logger.recordOutput("Shooter/Top/Velocity", shooterTop.encoder.velocity)
        Logger.recordOutput("Shooter/Top/Current", shooterTop.outputCurrent )
        Logger.recordOutput("Shooter/Top/Temperature", shooterTop.motorTemperature)

        Logger.recordOutput("Shooter/Bottom/Output", shooterBottom.appliedOutput)
        Logger.recordOutput("Shooter/Bottom/Velocity", shooterBottom.encoder.velocity)
        Logger.recordOutput("Shooter/Bottom/Current", shooterBottom.outputCurrent)
        Logger.recordOutput("Shooter/Bottom/Temperature", shooterBottom.motorTemperature)
    }
}