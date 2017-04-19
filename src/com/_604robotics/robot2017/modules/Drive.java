package com._604robotics.robot2017.modules;

import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Module {
    private final RobotDrive robotDrive = new RobotDrive(1, 2);

    private final Encoder encoderLeft = new Encoder(1, 2, false, CounterBase.EncodingType.k4X);
    private final Encoder encoderRight = new Encoder(3, 4, true, CounterBase.EncodingType.k4X);
    
    public final Output<Integer> leftClicks = addOutput("leftClicks", encoderLeft::get);
    public final Output<Integer> rightClicks = addOutput("rightClicks", encoderRight::get);

    private class Idle extends Action {
        public Idle () {
            super(Drive.this, Idle.class);
        }

        @Override
        public void run () {
            robotDrive.stopMotor();
        }
    }

    public final Action idle = new Idle();
    
    public class TankDrive extends Action {
        public final Input<Double> leftPower = addInput("leftPower", 0d);
        public final Input<Double> rightPower = addInput("rightPower", 0d);

        public TankDrive () {
            super(Drive.this, TankDrive.class);
        }

        public TankDrive (double initialLeftPower, double initialRightPower) {
            this();
            leftPower.set(initialLeftPower);
            rightPower.set(initialRightPower);
        }

        @Override
        public void run () {
            robotDrive.tankDrive(leftPower.get(), rightPower.get());
        }
    }

    public Drive () {
        super(Drive.class);
        setDefaultAction(idle);
    }
}