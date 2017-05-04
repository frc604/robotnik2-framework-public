package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Calibration;
import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class Shooter extends Module {

    private final Victor motor = new Victor(Ports.SHOOTER_MOTOR);
    private final Encoder encoder = new Encoder(Ports.ENCODER_SHOOTER_A,Ports.ENCODER_SHOOTER_B,
           false,CounterBase.EncodingType.k4X);
    private final Idle idle = new Idle();
    private final ShootAction shootAct = new ShootAction();
    public Input<Double> threshold;
    public Input<Double> rawPower;
    public Output<Double> calcPower = addOutput("Calculated Power", () -> shootAct.currentPower);
    public Output<Double> encoderRate = addOutput("Flywheel Speed", () -> encoder.getRate());

    private class Idle extends Action {
        public Idle () {
            super(Shooter.this, Idle.class);
        }

        @Override
        protected void run () {
            motor.stopMotor();
        }
    }

    public class ShootAction extends Action {
        public boolean sharp;
        public double currentPower;

        public ShootAction() {
            this(Calibration.SHOOTER_TARGET,false);
        }

        public ShootAction (double defaultThreshold, boolean sharp) {
            super(Shooter.this, ShootAction.class);
            this.sharp=sharp;
            threshold=addInput("Speed Threshold", defaultThreshold);
        }

        private static final double FACTOR_MULT = 1.14499756464; //calc=0.25

        public double calculate() {
            double val=threshold.get();
            double val_calc=val*FACTOR_MULT;
            val_calc/=val;
            val_calc++;
            val_calc=Math.sqrt(val_calc);
            return 1/val_calc;
        }

        @Override
        protected void run () {
            if (sharp) {
                currentPower = 1;
            } else {
                currentPower = calculate();
            }
            if (encoder.getRate()<threshold.get()) {
                motor.set(currentPower);
            } else {
                motor.stopMotor();
            }
        }

        @Override
        protected void end () {
            motor.stopMotor();
        }
    }

    public class RawShootAction extends Action {

        public RawShootAction() {
            this(0);
        }

        public RawShootAction (double defaultPower) {
            super(Shooter.this, ShootAction.class);
            rawPower=addInput("Raw Power", defaultPower);
        }

        @Override
        protected void run () {
            motor.set(rawPower.get());
        }

        @Override
        protected void end () {
            motor.stopMotor();
        }
    }

    public Shooter() {
        super(Shooter.class);
        setDefaultAction(idle);
    }

}