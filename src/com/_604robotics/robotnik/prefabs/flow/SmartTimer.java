package com._604robotics.robotnik.prefabs.flow;

import edu.wpi.first.wpilibj.Timer;

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

    public void runAfter (double time, Runnable runnable) {
        if (hasReachedTime(time)) {
            runnable.run();
        }
    }

    public void runEvery (double time, Runnable runnable) {
        if (hasReachedTime(time)) {
            runnable.run();
            reset();
        }
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
