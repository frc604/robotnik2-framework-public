package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Calibration;
import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import com._604robotics.robotnik.prefabs.devices.MultiOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Intake extends Module {
    private final MultiOutput motors = new MultiOutput(
            new Victor(Ports.INTAKE_LEFT_MOTOR),
            new Victor(Ports.INTAKE_RIGHT_MOTOR){{setInverted(true);}});

    private final DigitalInput leftSensor = new DigitalInput(Ports.INTAKE_LEFT_SENSOR_DIGITAL_INPUT);
    private final DigitalInput rightSensor = new DigitalInput(Ports.INTAKE_RIGHT_SENSOR_DIGITAL_INPUT);

    public final Input<Double> power = addInput("power", Calibration.INTAKE_POWER);

    public final Output<Boolean> gearDetected =
            addOutput("gearDetected", () -> leftSensor.get() && rightSensor.get());

    private class Idle extends Action {
        public Idle () {
            super(Intake.this, Idle.class);
        }

        @Override
        protected void run () {
            motors.stopMotor();
        }
    }

    public final Action idle = new Idle();

    private class Spin extends Action {
        private final double polarity;

        public Spin (String name, double polarity) {
            super(Intake.this, name);
            this.polarity = polarity;
        }

        @Override
        protected void run () {
            motors.set(power.get() * polarity);
        }

        @Override
        protected void end () {
            motors.stopMotor();
        }
    }

    public final Action suck = new Spin("Suck", -1);
    public final Action spit = new Spin("Spit", 1);

    public Intake () {
        super(Intake.class);
    }
}