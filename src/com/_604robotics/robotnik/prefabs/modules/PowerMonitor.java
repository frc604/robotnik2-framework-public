package com._604robotics.robotnik.prefabs.modules;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Logger;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import com._604robotics.robotnik.prefabs.flow.SmartTimer;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PowerMonitor extends Module {

    private static final PowerDistributionPanel panel = new PowerDistributionPanel(Ports.PDP_MODULE);
    public static Output<Double> totalCurrent;
    public static Output<Double> batteryVoltage;

    // Specifying <Double> results in an error
    // Creating a special EmptyOutput class causes ArrayStoreExceptions
    @SuppressWarnings("unchecked")
	public static Output<Double> [] currents = new Output[16];
    public static double [] currentLimit = {40, 40, 40, 40,
                                     20, 20, 20, 20,
                                     20, 20, 20, 20,
                                     40, 40, 40, 40};

    static {
        for (int i=0;i<currentLimit.length;i++) {
            currentLimit[i]*=0.8;
        }
    }

    private static final Logger theLogger = new Logger(PowerMonitor.class);
    private final SmartTimer iterTimer = new SmartTimer();

    public PowerMonitor() {
        super(PowerMonitor.class);
        totalCurrent=addOutput("Total Current", () -> panel.getTotalCurrent());
        batteryVoltage=addOutput("Battery Voltage", () -> panel.getVoltage());
        for (int port=0;port<=15;port++) {
        	int proxy=port;
            Output<Double> tempOutput = addOutput("Current "+port, () -> panel.getCurrent(proxy));
            currents[proxy]=tempOutput;
            
        }
    }
    
    private Runnable checkCurrent = new Runnable() {
        public void run() {
            for (int i=0;i<currents.length;i++) {
                if (currents[i].get() > currentLimit[i]) {
                    theLogger.log("WARN", "Excess current of "+currents[i].get()+" through PDP Port "+i+"!");
                }
            }
        }
    };
    
    protected void begin() {
        iterTimer.start();
    }
    
    protected void run() {
        // TODO: Find good time interval
        iterTimer.runEvery(0.5, checkCurrent);
    }
    
    protected void end() {
        iterTimer.stopAndReset();
    }

}
