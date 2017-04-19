package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Module;
import edu.wpi.first.wpilibj.Relay;

public class SignalLight extends Module {
    private final Relay light = new Relay(Ports.SIGNAL_LIGHT_RELAY);

    private class Off extends Action {
        public Off () {
            super(SignalLight.this, Off.class);
        }

        @Override
        protected void run () {
            light.set(Relay.Value.kOff);
        }
    }

    public final Action off = new Off();

    private class On extends Action {
        private On () {
            super(SignalLight.this, On.class);
        }

        @Override
        protected void run () {
            light.set(Relay.Value.kForward);
        }
    }

    public final Action on = new On();

    public SignalLight () {
        super(SignalLight.class);
        setDefaultAction(off);
    }
}
