// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package org.rambots.subsystems.drive;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.ParentDevice;
import org.littletonrobotics.junction.Logger;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static org.rambots.subsystems.drive.DriveConstants.odometryFrequency;

/**
 * Provides an interface for asynchronously reading high-frequency measurements to a set of queues.
 *
 * <p>This version is intended for Phoenix 6 devices on both the RIO and CANivore buses. When using
 * a CANivore, the thread uses the "waitForAll" blocking method to enable more consistent sampling.
 * This also allows Phoenix Pro users to benefit from lower latency between devices using CANivore
 * time synchronization.
 */
public class PhoenixOdometryThread extends Thread {
    private static PhoenixOdometryThread instance = null;
    private final Lock signalsLock =
            new ReentrantLock(); // Prevents conflicts when registering signals
    private final List<Queue<Double>> queues = new ArrayList<>();
    private final List<Queue<Double>> timestampQueues = new ArrayList<>();
    private BaseStatusSignal[] ctreSignals = new BaseStatusSignal[0];
    private List<Supplier<OptionalDouble>> otherSignals = new ArrayList<>();
    private final List<Queue<Double>> otherQueues = new ArrayList<>();
    private boolean isCANFD = false;

    private PhoenixOdometryThread() {
        setName("PhoenixOdometryThread");
        setDaemon(true);
    }

    public static PhoenixOdometryThread getInstance() {
        if (instance == null) {
            instance = new PhoenixOdometryThread();
        }
        return instance;
    }

    @Override
    public void start() {
        if (!timestampQueues.isEmpty()) {
            super.start();
        }
    }

    public Queue<Double> registerSignal(ParentDevice device, StatusSignal<Double> signal) {
        ArrayBlockingQueue<Double> queue = new ArrayBlockingQueue<>(10);
        signalsLock.lock();
        Drive.odometryLock.lock();
        try {
            isCANFD = CANBus.isNetworkFD(device.getNetwork());
            BaseStatusSignal[] newSignals = new BaseStatusSignal[ctreSignals.length + 1];
            System.arraycopy(ctreSignals, 0, newSignals, 0, ctreSignals.length);
            newSignals[ctreSignals.length] = signal;
            ctreSignals = newSignals;
            queues.add(queue);
        } finally {
            signalsLock.unlock();
            Drive.odometryLock.unlock();
        }
        return queue;
    }

    public Queue<Double> registerSignal(Supplier<OptionalDouble> signal) {
        Queue<Double> queue = new ArrayBlockingQueue<>(10);
        Drive.odometryLock.lock();
        try {
            otherSignals.add(signal);
            otherQueues.add(queue);
        } finally {
            Drive.odometryLock.unlock();
        }
        return queue;
    }

    public Queue<Double> makeTimestampQueue() {
        ArrayBlockingQueue<Double> queue = new ArrayBlockingQueue<>(10);
        Drive.odometryLock.lock();
        try {
            timestampQueues.add(queue);
        } finally {
            Drive.odometryLock.unlock();
        }
        return queue;
    }

    @Override
    public void run() {
        while (true) {
            // Wait for updates from all signals
            signalsLock.lock();
            try {
                if (isCANFD) {
                    BaseStatusSignal.waitForAll(2.0 / odometryFrequency, ctreSignals);
                } else {
                    // "waitForAll" does not support blocking on multiple
                    // signals with a bus that is not CAN FD, regardless
                    // of Pro licensing. No reasoning for this behavior
                    // is provided by the documentation.
                    Thread.sleep((long) (1000.0 / odometryFrequency));
                    if (ctreSignals.length > 0) BaseStatusSignal.refreshAll(ctreSignals);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                signalsLock.unlock();
            }

            // Save new data to queues
            Drive.odometryLock.lock();
            try {
                double timestamp = Logger.getRealTimestamp() / 1e6;
                double totalLatency = 0.0;
                for (BaseStatusSignal signal : ctreSignals) {
                    totalLatency += signal.getTimestamp().getLatency();
                }
                if (ctreSignals.length > 0) {
                    timestamp -= totalLatency / ctreSignals.length;
                }
                for (int i = 0; i < ctreSignals.length; i++) {
                    queues.get(i).offer(ctreSignals[i].getValueAsDouble());
                }

                double[] otherValues = new double[otherSignals.size()];
                boolean isValid = true;
                for (int i = 0; i < otherSignals.size(); i++) {
                    OptionalDouble value = otherSignals.get(i).get();
                    if (value.isPresent()) {
                        otherValues[i] = value.getAsDouble();
                    } else {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    for (int i = 0; i < otherQueues.size(); i++) {
                        otherQueues.get(i).offer(otherValues[i]);
                    }

                }

                for (Queue<Double> timestampQueue : timestampQueues) {
                    timestampQueue.offer(timestamp);
                }
            } finally {
                Drive.odometryLock.unlock();
            }
        }
    }
}
