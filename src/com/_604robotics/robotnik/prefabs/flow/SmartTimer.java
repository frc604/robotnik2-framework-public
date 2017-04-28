package com._604robotics.robotnik.prefabs.flow;

import edu.wpi.first.wpilibj.Timer;

import java.util.function.Supplier;

public class SmartTimer extends Timer {
    private boolean running;

    public boolean isRunning () {
        return running;
    }

    public void startIfNotRunning () {
        if (!isRunning()) {
            start();
        }
    }

    public void stopAndReset () {
        stop();
        reset();
    }

    public boolean hasReachedTime (double time) {
        return get() >= time;
    }

    public void runUntil (double time, Runnable runnable) {
        if (!hasReachedTime(time)) {
            runnable.run();
        }
    }

    public <T> T runUntil (double time, T defaultValue, Supplier<T> supplier) {
        if (!hasReachedTime(time)) {
            return supplier.get();
        }
        return defaultValue;
    }

    public <T> T runUntil (double time, Supplier<T> supplier) {
        return runUntil(time, null, supplier);
    }

    public void runAfter (double time, Runnable runnable) {
        if (hasReachedTime(time)) {
            runnable.run();
        }
    }

    public <T> T runAfter (double time, T defaultValue, Supplier<T> supplier) {
        if (hasReachedTime(time)) {
            return supplier.get();
        }
        return defaultValue;
    }

    public <T> T runAfter (double time, Supplier<T> supplier) {
        return runAfter(time, null, supplier);
    }

    public void runEvery (double time, Runnable runnable) {
        if (hasReachedTime(time)) {
            runnable.run();
            reset();
        }
    }

    public <T> T runEvery (double time, T defaultValue, Supplier<T> supplier) {
        if (hasReachedTime(time)) {
            final T value = supplier.get();
            reset();
            return value;
        }
        return defaultValue;
    }

    public <T> T runEvery (double time, Supplier<T> supplier) {
        return runEvery(time, null, supplier);
    }

    @Override
    public void start () {
        super.start();
        running = true;
    }

    @Override
    public void stop () {
        super.stop();
        running = false;
    }
}