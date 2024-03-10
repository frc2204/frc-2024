package org.rambots.config

import com.pathplanner.lib.util.PIDConstants

object ArmConstants {
    /** Arm Configuration */
    const val ARM_NEO_LEADER_ID = 13
    const val ARM_NEO_FOLLOWER_ID = 14

    val ARM_PID = PIDConstants(0.02, 0.0, 0.0)

    val ARM_ACCEPTABLE_ERROR = 0.5



    /** Elevator PID */
    const val ElevatorKP = 0.2
    const val ElevatorKI = 0.0
    const val ElevatorKD = 0.0

    /** Intake Configuration */
    const val IntakeMotorOneCANID = 17
    const val IntakeMotorTwoCANID = 18
    const val IntakePower = -0.2
    const val IntakeCurrent = 20

    /** Shooter Configuration */
    const val TopShooterCANID = 19
    const val BottomShooterCANID = 20

    /** Shooter PID */
    const val ShooterKP = 0.00035
    const val ShooterKD = 0.15

    /** Wrist Configuration */
    const val WristCANID = 21

    /** Wrist PID */
    const val WristKP = 0.2
    const val WristKI = 0.0
    const val WristKD = 0.0

    /** Intake Positions */
    const val ArmIntakePosition = 0.0
    const val WristIntakePosition = -63.0
    const val ElevatorIntakePosition = -50.0

    /** Climb Positions */
    const val ArmClimbPosition = -70.0
    const val ElevatorClimbPosition = -70.0
    const val ElevatorRetractPosition = 0.0

    /** Manual Arm Controls */
    // place holders
    const val MaximumAngle = 90
    const val MinimumAngle = 30
    const val ArmIdlePosition = -45.0

    /** Limelight */
    const val LimelightMountAngleDegrees = 25.0
    const val LimelightLensHeightInches = 20.0
    const val SpeakerGoalHeightInches = 50.0
}