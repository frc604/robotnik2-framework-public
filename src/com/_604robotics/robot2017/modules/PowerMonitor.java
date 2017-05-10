package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PowerMonitor extends Module {

    private final PowerDistributionPanel panel = new PowerDistributionPanel(Ports.PDP_MODULE);
    public Output<Double> totalPower;
    public Output<Double> batteryVoltage;
    public Output<Double> [] currents;// = new Output[16];

    public PowerMonitor() {
        super(PowerMonitor.class);
        totalPower=addOutput("Total Power", () -> panel.getTotalCurrent());
        batteryVoltage=addOutput("Battery Voltage", () -> panel.getVoltage());
        /*for (int port=0;port<=15;port++) {
            currents[port]=addOutput("Current "+port, () -> panel.getCurrent(port));
        }*/
        // Repetition in code *WHILE* the loop above doesn't work
        currents[0]=addOutput("Current 0", () -> panel.getCurrent(0));
        currents[1]=addOutput("Current 1", () -> panel.getCurrent(1));
        currents[2]=addOutput("Current 2", () -> panel.getCurrent(2));
        currents[3]=addOutput("Current 3", () -> panel.getCurrent(3));
        currents[4]=addOutput("Current 4", () -> panel.getCurrent(4));
        currents[5]=addOutput("Current 5", () -> panel.getCurrent(5));
        currents[6]=addOutput("Current 6", () -> panel.getCurrent(6));
        currents[7]=addOutput("Current 7", () -> panel.getCurrent(7));
        currents[8]=addOutput("Current 8", () -> panel.getCurrent(8));
        currents[9]=addOutput("Current 9", () -> panel.getCurrent(9));
        currents[10]=addOutput("Current 10", () -> panel.getCurrent(10));
        currents[11]=addOutput("Current 11", () -> panel.getCurrent(11));
        currents[12]=addOutput("Current 12", () -> panel.getCurrent(12));
        currents[13]=addOutput("Current 13", () -> panel.getCurrent(13));
        currents[14]=addOutput("Current 14", () -> panel.getCurrent(14));
        currents[15]=addOutput("Current 15", () -> panel.getCurrent(15));

    }

}
