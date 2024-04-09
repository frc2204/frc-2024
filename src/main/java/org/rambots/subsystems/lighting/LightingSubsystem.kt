package org.rambots.subsystems

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.rambots.Robot
import org.rambots.RobotContainer
import org.rambots.subsystems.lighting.LimelightLEDState
import org.rambots.util.LimelightHelpers

object LightingSubsystem : SubsystemBase() {

    private var limelightLEDState = LimelightLEDState.LIMELIGHT_OFF

    private const val limelightName = "limelight-three"

    fun blindLL() { // "Blindlight"
        limelightLEDState = LimelightLEDState.LIMELIGHT_ON
        LimelightHelpers.setLEDMode_ForceOn(limelightName)
    }

    fun blinkLL() {
        limelightLEDState = LimelightLEDState.LIMELIGHT_BLINK
        LimelightHelpers.setLEDMode_ForceBlink(limelightName)
    }

    fun clearLL() {
        limelightLEDState = LimelightLEDState.LIMELIGHT_OFF
        LimelightHelpers.setLEDMode_ForceOff(limelightName)
    }

    fun power(double: Double) {

    }

    override fun periodic() {

        Logger.recordOutput("Lighting/LimelightLEDState", limelightLEDState.name)

        if (Robot.isDisabled) {
            if (RobotContainer.hasLocalized && RobotContainer.hasActiveTag) {
                blindLL()
            } else if (RobotContainer.hasLocalized) {
                blinkLL()
            } else {
                clearLL()
            }

        }


    }

}