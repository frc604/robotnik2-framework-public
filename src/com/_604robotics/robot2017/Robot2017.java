package com._604robotics.robot2017;

import com._604robotics.robot2017.modes.AutonomousMode;
import com._604robotics.robot2017.modes.TeleopMode;
import com._604robotics.robot2017.modules.Drive;
import com._604robotics.robotnik.Robot;

public class Robot2017 extends Robot {
    public final Drive drive = addModule(new Drive());

	public Robot2017 () {
	    setAutonomousMode(new AutonomousMode(this));
	    setTeleopMode(new TeleopMode(this));
	}
}