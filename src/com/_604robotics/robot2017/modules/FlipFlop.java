package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Calibration;
import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class FlipFlop extends Module {
    private final DoubleSolenoid piston =
            new DoubleSolenoid(Ports.FLIP_FLOP_EXTEND_SOLENOID, Ports.FLIP_FLOP_RETRACT_SOLENOID);

    private final Timer timer = new Timer();
    private boolean startup = true;

    public final Input<Double> transitionTime = addInput("transitionTime", Calibration.FLIP_FLOP_TRANSITION_TIME);

    private class Actuate extends Action {
        private final DoubleSolenoid.Value direction;

        public Actuate (String name, DoubleSolenoid.Value direction) {
            super(FlipFlop.this, name);
            this.direction = direction;
        }

        @Override
        protected void begin () {
            timer.reset();
            piston.set(direction);
        }

        @Override
        protected void end () {
            startup = false;
        }
    }

    public final Action retract = new Actuate("Retract", DoubleSolenoid.Value.kReverse);
    public final Action extend = new Actuate("Extend", DoubleSolenoid.Value.kForward);

    public final Output<Boolean> retracted =
            addOutput("retracted", () -> retract.isRunning() && (startup || timer.get() >= transitionTime.get()));
    public final Output<Boolean> extended =
            addOutput("extended", () -> extend.isRunning() && timer.get() >= transitionTime.get());

    public FlipFlop () {
        super(FlipFlop.class);
        setDefaultAction(retract);
    }

    @Override
    protected void begin () {
        startup = true;
    }
}
