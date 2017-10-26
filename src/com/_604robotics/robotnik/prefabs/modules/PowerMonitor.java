package com._604robotics.robotnik.prefabs.modules;

import com._604robotics.robotnik.Logger;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import com._604robotics.robotnik.prefabs.flow.Pulse;
import com._604robotics.robotnik.prefabs.flow.SmartTimer;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

//UNTESTED
//             _            _           _
// _   _ _ __ | |_ ___  ___| |_ ___  __| |
//| | | | '_ \| __/ _ \/ __| __/ _ \/ _` |
//| |_| | | | | ||  __/\__ \ ||  __/ (_| |
// \__,_|_| |_|\__\___||___/\__\___|\__,_|
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

    public static double voltageLimit = 7.5;

    static {
        for (int i = 0; i < currentLimit.length; i++) {
            currentLimit[i] *= 0.8;
        }
    }

    private SmartTimer [] timerArray = new SmartTimer[NUM_CURRENT_PORTS];
    private SmartTimer voltageTimer = new SmartTimer();
    private Pulse voltagePulse = new Pulse();
    private Pulse [] pulseArray = new Pulse[NUM_CURRENT_PORTS];
    
    private static final Logger theLogger = new Logger(PowerMonitor.class);

    private Runnable checkCurrent = new Runnable() {
        public void run() {
            for (int i=0;i<currents.length;i++) {
                if (currents[i].get() > currentLimit[i]) {
                    theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                }
            }
        }
    };

    private void resetTimers() {
        for (int i=0;i<NUM_CURRENT_PORTS;i++) {
            timerArray[i]=new SmartTimer();
            timerArray[i].stopAndReset();
            pulseArray[i]=new Pulse();
            pulseArray[i].update(false);
        }
        voltageTimer.stopAndReset();
        voltagePulse.update(false);
    }

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
        for (Output<Double> test:currents) {
            if (test==null) {
                theLogger.log("WARN","null tempOutput");
            }
        }
    }
    protected void begin() {
        resetTimers();
    }

    protected void run() {
        for (int i=0;i<NUM_CURRENT_PORTS;i++) {
            if (timerArray[i].isRunning()) {
                if ((timerArray[i].get()%0.5)<0.001 && (timerArray[i].get()>0.4)) {
                    theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                    theLogger.log("WARN", "Excess has continued for "+timerArray[i].get()+" seconds.");
                }
            }
        }
        if (voltageTimer.isRunning()) {
            if ((voltageTimer.get()%0.5)<0.001 && (voltageTimer.get()>0.4)) {
                theLogger.log("WARN", "Low voltage of "+batteryVoltage.get()+"V!");
                theLogger.log("WARN", "Voltage drop has continued for "+voltageTimer.get()+" seconds.");
            }
        }
        for (int i=0;i<NUM_CURRENT_PORTS;i++) {
            if (currents[i].get() > currentLimit[i]) {
                pulseArray[i].update(true);
                timerArray[i].start();
                // Do not print start since such transients occur during normal operation
                if (pulseArray[i].isRisingEdge()) {
                    //theLogger.log("WARN", "Excess current started through PDP Port "+i+"!");
                }
            } else {
                pulseArray[i].update(false);
                timerArray[i].stop();
                // Print only when this is not a transient
                if (pulseArray[i].isFallingEdge() && timerArray[i].get()>0.4) {
                    theLogger.log("WARN", "Excess current ended through PDP Port "+i+" after "+timerArray[i].get()+" seconds.");
                }
                timerArray[i].reset();
            }
        }
        if (batteryVoltage.get()<voltageLimit) {
            voltagePulse.update(true);
            voltageTimer.start();
            if (voltagePulse.isRisingEdge()) {
                theLogger.log("WARN", "Voltage has dropped below "+voltageLimit+"V!");
            }
        } else {
            voltagePulse.update(false);
            voltageTimer.stop();
            if (voltagePulse.isFallingEdge()) {
                theLogger.log("WARN", "Voltage has risen above "+voltageLimit+"V!");
            }
        }
        for (SmartTimer timer : timerArray) {
            timer.stopAndReset();
        }
    }

    protected void end() {
        resetTimers();
    }

    private class MonitorThread extends Thread {
        private boolean keepRunning=true;
        private SmartTimer [] timerArray = new SmartTimer[NUM_CURRENT_PORTS];
        private SmartTimer voltageTimer = new SmartTimer();
        private Pulse voltagePulse = new Pulse();
        private Pulse [] pulseArray = new Pulse[NUM_CURRENT_PORTS];

        public void stopRun() {
            // Ignoring potential race condition here
            // Does not matter if run loop runs one more time than it should
            keepRunning=false;
        }
        @Override
        public void run() {
            theLogger.info("Starting Power Monitoring Thread");
            for (int i=0;i<NUM_CURRENT_PORTS;i++) {
                timerArray[i]=new SmartTimer();
                timerArray[i].stopAndReset();
                pulseArray[i]=new Pulse();
                pulseArray[i].update(false);
            }
            voltageTimer.stopAndReset();
            voltagePulse.update(false);

            for (int port=0;port<NUM_CURRENT_PORTS;port++) {
                int proxy=port;
                Output<Double> tempOutput = addOutput("Port "+port+" Current", () -> panel.getCurrent(proxy));
                currents[proxy]=tempOutput;
            }
            double[] currentLimit = {40, 40, 40, 40,
                    20, 20, 20, 20,
                    20, 20, 20, 20,
                    40, 40, 40, 40};

            for (int i=0;i<NUM_CURRENT_PORTS;i++) {
                currentLimit[i]*=0.8;
            }

            // Main monitor loop
            while (keepRunning && !isInterrupted()) {
                for (int i=0;i<NUM_CURRENT_PORTS;i++) {
                    if (timerArray[i].isRunning()) {
                        if ((timerArray[i].get()%0.5)<0.001 && (timerArray[i].get()>0.4)) {
                            theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                            theLogger.log("WARN", "Excess has continued for "+timerArray[i].get()+" seconds.");
                        }
                    }
                }
                if (voltageTimer.isRunning()) {
                    if ((voltageTimer.get()%0.5)<0.001 && (voltageTimer.get()>0.4)) {
                        theLogger.log("WARN", "Low voltage of "+batteryVoltage.get()+"V!");
                        theLogger.log("WARN", "Voltage drop has continued for "+voltageTimer.get()+" seconds.");
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
                if (batteryVoltage.get()<voltageLimit) {
                    voltagePulse.update(true);
                    voltageTimer.start();
                    if (voltagePulse.isRisingEdge()) {
                        theLogger.log("WARN", "Voltage has dropped below "+voltageLimit+"V!");
                    }
                } else {
                    voltagePulse.update(false);
                    voltageTimer.stop();
                    if (voltagePulse.isFallingEdge()) {
                        theLogger.log("WARN", "Voltage has risen above "+voltageLimit+"V!");
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
