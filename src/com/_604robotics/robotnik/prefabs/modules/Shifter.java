package com._604robotics.robotnik.prefabs.modules;

import java.util.HashMap;

import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Module;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Shifter extends Module {

	private final DoubleSolenoid solenoid;

	public Shifter(int forwardSolenoid, int reverseSolenoid) {
		super(Shifter.class);
		this.solenoid=new DoubleSolenoid(forwardSolenoid,reverseSolenoid);
		setDefaultAction(lowGear);
	}
	
	
	private class SetGear extends Action {
		private Value currentState;
		public SetGear(Value initState) {
			super(Shifter.this, SetGear.class);
			currentState=initState;
		}
		
		public void run() {
			solenoid.set(currentState);
		}
	}
	
	public final Action lowGear=new SetGear(Value.kReverse);
	public final Action highGear=new SetGear(Value.kForward);
}
