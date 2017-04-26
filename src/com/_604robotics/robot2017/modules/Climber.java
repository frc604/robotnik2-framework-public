package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import edu.wpi.first.wpilibj.Victor;

public class Climber extends Module {
    private final Victor motor = new Victor(Ports.CLIMBER_MOTOR);

    private class Idle extends Action {
        public Idle () {
            super(Climber.this, Idle.class);
        }

        @Override
        protected void run () {
            motor.stopMotor();
        }
    }

    public final Action idle = new Idle();

    public class Climb extends Action {
        public final Input<Double> power;

        public Climb () {
            this(0);
        }

        public Climb (double defaultPower) {
            super(Climber.this, Climb.class);
            power = addInput("power", defaultPower);
        }

        @Override
        protected void run () {
            motor.set(power.get());
        }

        @Override
        protected void end () {
            motor.stopMotor();
        }
    }

    public Climber () {
        super(Climber.class);
        setDefaultAction(idle);
    }
}