package org.rambots.subsystems.drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

import java.util.OptionalDouble;
import java.util.Queue;

public class GyroIO_ADIS implements GyroIO {
    private final ADIS16470_IMU gyro = new ADIS16470_IMU();
    private final Queue<Double> yawPositionQueue;

    public GyroIO_ADIS() {
        gyro.reset();

        yawPositionQueue =
                PhoenixOdometryThread.getInstance()
                        .registerSignal(
                                () -> {
                                    boolean valid = gyro.isConnected();
                                    if (valid) {
                                        return OptionalDouble.of(gyro.getAngle());
                                    } else {
                                        return OptionalDouble.empty();
                                    }
                                });
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
        inputs.connected = gyro.isConnected();
        inputs.yawPosition = Rotation2d.fromDegrees(gyro.getAngle());
        inputs.yawVelocityRadPerSec = Units.degreesToRadians(gyro.getRate());
        inputs.odometryYawPositions =
                yawPositionQueue.stream().map(Rotation2d::fromDegrees).toArray(Rotation2d[]::new);

        yawPositionQueue.clear();
    }

}
