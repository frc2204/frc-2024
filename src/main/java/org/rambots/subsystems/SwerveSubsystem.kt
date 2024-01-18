package org.rambots.subsystems

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.wpilibj.ADIS16470_IMU
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.simulation.ADIS16470_IMUSim
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.rambots.Robot
import org.rambots.config.SwerveConstants.INVERTED_GYRO
import org.rambots.config.SwerveConstants.KINEMATICS
import org.rambots.config.SwerveConstants.MAX_SPEED
import org.rambots.config.SwerveConstants.STATE_STANDARD_DEVIATIONS
import org.rambots.config.SwerveConstants.VISION_STANDARD_DEVIATIONS
import org.rambots.config.SwerveModuleSettings
import org.rambots.lib.swerve.SwerveModule

object SwerveSubsystem : SubsystemBase() {
    private val IMU = ADIS16470_IMU()
    /* simulation imu */
    private val adis16470ImuSim = ADIS16470_IMUSim(IMU)

    private val swerveModules = arrayOf(
        SwerveModule(SwerveModuleSettings.FRONT_LEFT),
        SwerveModule(SwerveModuleSettings.FRONT_RIGHT),
        SwerveModule(SwerveModuleSettings.BACK_LEFT),
        SwerveModule(SwerveModuleSettings.BACK_RIGHT)
    )

    /* 2D representation of game field for dashboards */
    private val field = Field2d()

    /* returns the yaw angle in degrees (CCW positive). */
    private val gyroAngle get() = IMU.getAngle(ADIS16470_IMU.IMUAxis.kYaw)

    /* returns yaw angle as Rotation2d object */
    private val yaw: Rotation2d get() = Rotation2d.fromDegrees(if (INVERTED_GYRO) 360 - gyroAngle else gyroAngle)

    private var poseEstimator: SwerveDrivePoseEstimator

    /* Returns the estimated robot pose in meters */
    val pose: Pose2d get() = poseEstimator.estimatedPosition

    /* autonomous */
    var swerveModuleStates
        /* getter returns an array of SwerveModuleState with individual swerve state (speed, angle) */
        get() = swerveModules.map {it.getState()}.toTypedArray()
        set(value) {
            /* renormalizes speed of each swerve module if any individual speed is above the specified maximum */
            SwerveDriveKinematics.desaturateWheelSpeeds(value, MAX_SPEED)
            /* set desired state for each module */
            swerveModules.forEachIndexed {index, module -> module.setDesiredState(value[index], false)}
        }

    init {
        /* avoids bug with inverted motors */
        Timer.delay(1.0)
        /* steer motor positions set to absolute positions */
        resetModulesToAbsolute()

        poseEstimator = SwerveDrivePoseEstimator(
            KINEMATICS,
            yaw,
            getModulePositions(),
            Pose2d(0.0, 0.0, Rotation2d()),
            STATE_STANDARD_DEVIATIONS,
            VISION_STANDARD_DEVIATIONS,
        )
        SmartDashboard.putData("Field", field)
    }

    fun drive(translation: Translation2d, rotation: Double, fieldRelative: Boolean, isOpenLoop: Boolean) {
        /* calculates chassis speeds based off field relativity */
        val chassisSpeeds = if (fieldRelative) {
            ChassisSpeeds.fromFieldRelativeSpeeds(
                translation.x,
                translation.y,
                rotation,
                yaw
            )
        } else {
            ChassisSpeeds(translation.x, translation.y, rotation)
        }

        /* converting calculated chassis speeds into individual module speed */
        val swerveModuleStates = KINEMATICS.toSwerveModuleStates(chassisSpeeds)
        /* scaling down maximum speed just in case one module exceeds speed limit */
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, MAX_SPEED)

        /* for each module, set the desired speed state with the new calculated states */
        swerveModules.forEachIndexed { index, module -> module.setDesiredState(swerveModuleStates[index], isOpenLoop) }

        if(Robot.simulation) {
            IMU.setGyroAngleZ(gyroAngle + chassisSpeeds.omegaRadiansPerSecond * 0.02 * 180 / Math.PI)
        }
    }

    /* resets the gyro accumulations to a heading of zero */
    fun zeroGyro(): Command = runOnce {
        IMU.reset()
    }

    private fun getModulePositions(): Array<SwerveModulePosition> {
        return swerveModules.map { it.getPosition() }.toTypedArray<SwerveModulePosition>()
    }

    private fun resetModulesToAbsolute() {
        swerveModules.forEach { it.resetToAbsolute() }
    }

    override fun periodic() {
        /* updates estimated robot position on the field */
        poseEstimator.update(yaw, getModulePositions())

        /* sets robot pose to estimated pose */
        field.robotPose = pose

        SmartDashboard.putBoolean("Gyro Connected", IMU.isConnected)
        SmartDashboard.putNumber("Gyro", gyroAngle)
    }
}