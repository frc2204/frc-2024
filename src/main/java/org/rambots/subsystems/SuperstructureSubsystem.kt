package org.rambots.subsystems

object SuperstructureSubsystem {
    /** Intake */
    fun intake() {
        WristSubsystem.wristIntakePosition()
        ArmSubsystem.armIntakePosition()
    }
}