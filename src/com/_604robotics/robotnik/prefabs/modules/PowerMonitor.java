package com._604robotics.robotnik.prefabs.modules;

import com._604robotics.robotnik.Logger;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import com._604robotics.robotnik.prefabs.flow.Pulse;
import com._604robotics.robotnik.prefabs.flow.SmartTimer;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PowerMonitor extends Module {

    private final PowerDistributionPanel panel;
    private final Compressor compressor;
    
    public static final int NUM_CURRENT_PORTS = 16;
    public Output<Double> totalPortCurrent;
    public Output<Double> totalCurrent;
    public Output<Double> batteryVoltage;

    // Specifying <Double> results in an error
    // Creating a special EmptyOutput class causes ArrayStoreExceptions
    @SuppressWarnings("unchecked")
    public Output<Double>[] currents = new Output[NUM_CURRENT_PORTS];
    public Output<Double> compCurrent;

    // While measured values may change, the current limits on each PDP stays the same
    public static double[] currentLimit = {40, 40, 40, 40,
                                           20, 20, 20, 20,
                                           20, 20, 20, 20,
                                           40, 40, 40, 40};

    static {
        for (int i = 0; i < currentLimit.length; i++) {
            currentLimit[i] *= 0.8;
        }
    }

    private static final Logger theLogger = new Logger(PowerMonitor.class);
    private final SmartTimer iterTimer = new SmartTimer();
    private MonitorThread monThread;
    
    private Runnable checkCurrent = new Runnable() {
        public void run() {
            for (int i=0;i<currents.length;i++) {
                if (currents[i].get() > currentLimit[i]) {
                    theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                }
            }
        }
    };
    
    public PowerMonitor(int PDPPortID, int compressorID) {
        super(PowerMonitor.class);
        // Initialize WPILib Objects
        panel = new PowerDistributionPanel(PDPPortID);
        compressor = new Compressor(compressorID);
        // Set up outputs
        compCurrent = addOutput("Compressor Current", () -> compressor.getCompressorCurrent());
        totalPortCurrent=addOutput("Total Port Current", () -> panel.getTotalCurrent());
        totalCurrent=addOutput("Total Current", () -> panel.getTotalCurrent()+compressor.getCompressorCurrent());
        batteryVoltage=addOutput("Battery Voltage", () -> panel.getVoltage());
        for (int port=0;port<NUM_CURRENT_PORTS;port++) {
            int proxy=port;
            Output<Double> tempOutput = addOutput("Port "+port+" Current", () -> panel.getCurrent(proxy));
            currents[proxy]=tempOutput;
        }
    }
    protected void begin() {
        iterTimer.start();
        monThread = new MonitorThread();
        // Minimum priority as monitoring is only a small part of all actions
        monThread.setPriority(Thread.MIN_PRIORITY);
        monThread.start();
    }
    
    protected void run() {
        // Thread does the monitoring
    }
    
    protected void end() {
        iterTimer.stopAndReset();
        monThread.stopRun();
        try {
            monThread.wait(25);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    private class MonitorThread extends Thread {
        private boolean keepRunning=true;
        private SmartTimer [] timerArray = new SmartTimer[NUM_CURRENT_PORTS];
        private Pulse [] pulseArray = new Pulse[NUM_CURRENT_PORTS];

        public void stopRun() {
            // Ignoring potential race condition here
            // Does not matter if run loop runs one more time than it should
            keepRunning=false;
        }
        @Override
        public void run() {
            theLogger.info("Starting Power Monitoring Thread");
            for (SmartTimer timer : timerArray) {
                timer.stopAndReset();
            }
            for (Pulse pulse : pulseArray) {
                pulse.update(false);
            }
            while (keepRunning && !isInterrupted()) {
                for (int i=0;i<NUM_CURRENT_PORTS;i++) {
                    if (timerArray[i].isRunning()) {
                        if ((timerArray[i].get()%0.5)<0.001 && (timerArray[i].get()>0.4)) {
                            theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                            theLogger.log("WARN", "Excess has continued for "+timerArray[i].get()+" seconds.");
                        }
                    }
                }
                Thread.yield();
                for (int i=0;i<NUM_CURRENT_PORTS;i++) {
                    if (currents[i].get() > currentLimit[i]) {
                        pulseArray[i].update(true);
                        timerArray[i].start();
                        if (pulseArray[i].isRisingEdge()) {
                            theLogger.log("WARN", "Excess current started through PDP Port "+i+"!");
                        }
                    } else {
                        pulseArray[i].update(false);
                        timerArray[i].stop();
                        if (pulseArray[i].isFallingEdge()) {
                            theLogger.log("WARN", "Excess current ended through PDP Port "+i+" after "+timerArray[i].get()+" seconds.");
                        }
                        timerArray[i].reset();
                    }
                }
                Thread.yield();
            }
            for (SmartTimer timer : timerArray) {
                timer.stopAndReset();
            }
            theLogger.info("Stopping Power Monitoring Thread");
        }
    }
}
