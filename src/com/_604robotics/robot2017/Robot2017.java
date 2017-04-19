package com._604robotics.robot2017;

import com._604robotics.robot2017.modes.AutonomousMode;
import com._604robotics.robot2017.modes.TeleopMode;
import com._604robotics.robot2017.modules.*;
import com._604robotics.robot2017.systems.DashboardSystem;
import com._604robotics.robotnik.Robot;

public class Robot2017 extends Robot {
    public final Dashboard dashboard = addModule(new Dashboard());
    public final Drive drive = addModule(new Drive());
    public final Climber climber = addModule(new Climber());
    public final SignalLight signalLight = addModule(new SignalLight());
    public final FlipFlop flipFlop = addModule(new FlipFlop());
    public final Intake intake = addModule(new Intake());

	public Robot2017 () {
	    addSystem(new DashboardSystem(this));

	    setAutonomousMode(new AutonomousMode(this));
	    setTeleopMode(new TeleopMode(this));
	}
}