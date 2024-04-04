package org.rambots.subsystems.drive;

import com.pathplanner.lib.util.PIDConstants;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import org.rambots.Constants;

/**
 * All Constants Measured in Meters and Radians (m/s, m/s^2, rad/s, rad/s^2)
 */
public final class DriveConstants {
    public static final double wheelRadius = Units.inchesToMeters(2.0);
    public static final double odometryFrequency =
            switch (Constants.getRobot()) {
                case SIMBOT -> 50.0;
                case COMPBOT -> 250.0;
            };
    public static final Matrix<N3, N1> stateStdDevs =
            switch (Constants.getRobot()) {
                default -> new Matrix<>(VecBuilder.fill(0.003, 0.003, 0.0002));
            };
    public static final double xyStdDevCoefficient =
            switch (Constants.getRobot()) {
                default -> 0.065;
            };
    public static final double thetaStdDevCoefficient =
            switch (Constants.getRobot()) {
                default -> 0.01;
            };
    // Turn to "" for no canbus name
    public static final String canbus = "*";
    public static final ModuleConstants moduleConstants =
            switch (Constants.getRobot()) {
                case COMPBOT -> new ModuleConstants(
                        0.1,
                        0.13,
                        0.07,
                        0.0,
                        5.0,
                        0.0,
                        SwerveXReductions.OldGen736.reduction,
                        SwerveXReductions.TURN.reduction);
                case SIMBOT -> new ModuleConstants(
                        0.014,
                        0.134,
                        0.1,
                        0.0,
                        10.0,
                        0.0,
                        SwerveXReductions.OldGen736.reduction,
                        SwerveXReductions.TURN.reduction);
            };
    public static final PIDConstants PPTranslationConstants =
            switch (Constants.getRobot()) {
                case COMPBOT -> new PIDConstants(10, 0.0, 0.0);
                case SIMBOT -> new PIDConstants(10, 0.0, 0.0);
            };
    public static final PIDConstants PPRotationConstants =
            switch (Constants.getRobot()) {
                case COMPBOT -> new PIDConstants(10, 0.0, 0.0);
                case SIMBOT -> new PIDConstants(10, 0.0, 0.0);
            };
    public static DrivetrainConfig drivetrainConfig =
            switch (Constants.getRobot()) {
                default -> new DrivetrainConfig(
                        Units.inchesToMeters(2.0),
                        Units.inchesToMeters(21.251),
                        Units.inchesToMeters(21.251),
                        Units.feetToMeters(14.21),
                        Units.feetToMeters(21.32),
                        12.93,
                        29.89);
            };
    public static final Translation2d[] moduleTranslations = new Translation2d[]{
            new Translation2d(drivetrainConfig.trackwidthX() / 2.0, drivetrainConfig.trackwidthY() / 2.0),
            new Translation2d(drivetrainConfig.trackwidthX() / 2.0, -drivetrainConfig.trackwidthY() / 2.0),
            new Translation2d(-drivetrainConfig.trackwidthX() / 2.0, drivetrainConfig.trackwidthY() / 2.0),
            new Translation2d(-drivetrainConfig.trackwidthX() / 2.0, -drivetrainConfig.trackwidthY() / 2.0)
    };

    public static final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(moduleTranslations);
    public static ModuleConfig[] moduleConfigs =
            switch (Constants.getRobot()) {
                case COMPBOT -> new ModuleConfig[]{
                        new ModuleConfig(1, 2, 9, Rotation2d.fromRotations(.223).plus(Rotation2d.fromDegrees(180)), true),
                        new ModuleConfig(3, 4, 10, Rotation2d.fromRotations(-.039).plus(Rotation2d.fromDegrees(180)), true),
                        new ModuleConfig(5, 6, 11, Rotation2d.fromRotations(-.394).plus(Rotation2d.fromDegrees(180)), true),
                        new ModuleConfig(7, 8, 12, Rotation2d.fromRotations(-0.15).plus(Rotation2d.fromDegrees(180)), true)
                };
                case SIMBOT -> {
                    ModuleConfig[] configs = new ModuleConfig[4];
                    for (int i = 0; i < configs.length; i++)
                        configs[i] = new ModuleConfig(0, 0, 0, new Rotation2d(0), false);
                    yield configs;
                }
            };
    public static HeadingControllerConstants headingControllerConstants =
            switch (Constants.getRobot()) {
                case COMPBOT -> new HeadingControllerConstants(1.7, 0.0);
                case SIMBOT -> new HeadingControllerConstants(2.0, 0.0);
            };

    private enum SwerveXReductions {
        OldGen736((42.0 / 11.0) * (18.0 / 28.0) * (45.0 / 15.0)),
        TURN(15.43);

        final double reduction;

        SwerveXReductions(double reduction) {
            this.reduction = reduction;
        }
    }

    public record DrivetrainConfig(
            double wheelRadius,
            double trackwidthX,
            double trackwidthY,
            double maxLinearVelocity,
            double maxLinearAcceleration,
            double maxAngularVelocity,
            double maxAngularAcceleration) {
        public double driveBaseRadius() {
            return Math.hypot(trackwidthX / 2.0, trackwidthY / 2.0);
        }
    }

    public record ModuleConfig(
            int driveID,
            int turnID,
            int absoluteEncoderChannel,
            Rotation2d absoluteEncoderOffset,
            boolean turnMotorInverted) {
    }

    public record ModuleConstants(
            double ffKs,
            double ffKv,
            double driveKp,
            double drivekD,
            double turnKp,
            double turnkD,
            double driveReduction,
            double turnReduction) {
    }

    public record HeadingControllerConstants(double Kp, double Kd) {
    }
}
