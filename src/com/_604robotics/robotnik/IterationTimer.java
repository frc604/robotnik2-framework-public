package com._604robotics.robotnik;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.Timer;

class IterationTimer {
    private final Timer timer = new Timer();
    private final double reportInterval;
    private long iterationCount;

    public IterationTimer (double reportInterval) {
        this.reportInterval = reportInterval;
    }
    
    public void start () {
        timer.start();
    }
    
    public void stop () {
        timer.stop();
        timer.reset();
        
        iterationCount = 0;
    }
    
    public void sample (Consumer<Double> report) {
        if (reportInterval == 0) {
            return;
        }

        ++iterationCount;
        
        if (timer.get() >= reportInterval) {
            report.accept(timer.get() / (double) iterationCount);
            iterationCount = 0;
            timer.reset();
        }
    }
}