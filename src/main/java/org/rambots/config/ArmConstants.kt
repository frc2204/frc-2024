package org.rambots.config

object ArmConstants {
    /** Arm Configuration */
    const val ArmMotorOneCANID = 13
    const val ArmMotorTwoCANID = 14
    const val WristMotorCANID = 2
    const val ArmKP = 0.02
    const val ArmKI = 0.0
    const val ArmKD = 0.0
    const val WristKP = 1.0
    const val WristKI = 0.0
    const val WristKD = 0.0

    /** Elevator Configuration */
    const val ElevatorMotorOneCANID = 3
    const val ElevatorMotorTwoCANID = 4
    const val ElevatorKP = 1.0
    const val ElevatorKI = 0.0
    const val ElevatorKD = 0.0

    /** Intake Configuration */
    const val IntakeMotorCANID = 15
    const val IntakeKP = 1.0
    const val IntakePower = -0.2

    /** Intake Positions */
    const val ArmIntakePosition = 1.0
    const val WristIntakePosition = 1.0
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