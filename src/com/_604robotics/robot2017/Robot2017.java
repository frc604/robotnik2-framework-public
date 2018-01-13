package com._604robotics.robot2017;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robot2017.modes.AutonomousMode;
import com._604robotics.robot2017.modes.TeleopMode;
import com._604robotics.robot2017.modules.*;
import com._604robotics.robot2017.systems.DashboardSystem;
import com._604robotics.robot2017.systems.PickupSystem;
import com._604robotics.robotnik.Robot;
import com._604robotics.robotnik.prefabs.modules.PowerMonitor;
import com._604robotics.robotnik.prefabs.modules.Shifter;

public class Robot2017 extends Robot {
    public final Dashboard dashboard = addModule(new Dashboard());
    public final Drive drive = addModule(new Drive());
    public final Climber climber = addModule(new Climber());
    public final SignalLight signalLight = addModule(new SignalLight());
    public final FlipFlop flipFlop = addModule(new FlipFlop());
    public final Intake intake = addModule(new Intake());
    public final PowerMonitor powerMonitor = addModule(new PowerMonitor(Ports.PDP_MODULE,Ports.COMPRESSOR));
    public final Shifter shifter = addModule(new Shifter(Ports.SHIFTER_FORWARD, Ports.SHIFTER_REVERSE));
    //public final Shooter shooter = addModule(new Shooter());
    public final Loader loader = addModule(new Loader());
    
    
    public Robot2017 () {
        setAutonomousMode(new AutonomousMode(this));
        setTeleopMode(new TeleopMode(this));

        addSystem(DashboardSystem.class, new DashboardSystem(this));
        addSystem(PickupSystem.class, new PickupSystem(this));
    }
}
