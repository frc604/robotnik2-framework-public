package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Ports;
import com._604robotics.robotnik.Action;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.Output;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive extends Module {
    // WARNING: CONFLICTS WITH Climber!
    private final RobotDrive robotDrive = new RobotDrive(
            Ports.DRIVE_FRONT_LEFT_MOTOR,
            Ports.DRIVE_REAR_LEFT_MOTOR,
            Ports.DRIVE_FRONT_RIGHT_MOTOR,
            Ports.DRIVE_REAR_RIGHT_MOTOR);

    private final Encoder encoderLeft = new Encoder(Ports.ENCODER_LEFT_A,
            Ports.ENCODER_LEFT_B,
            false,
            CounterBase.EncodingType.k4X);
    private final Encoder encoderRight = new Encoder(Ports.ENCODER_RIGHT_A,
            Ports.ENCODER_RIGHT_B,
            true,
            CounterBase.EncodingType.k4X);

    public final Output<Integer> leftClicks = addOutput("leftClicks", encoderLeft::get);
    public final Output<Integer> rightClicks = addOutput("rightClicks", encoderRight::get);

    public class Idle extends Action {
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
        public final Input<Double> leftPower;
        public final Input<Double> rightPower;

        public TankDrive () {
            this(0, 0);
        }

        public TankDrive (double defaultLeftPower, double defaultRightPower) {
            super(Drive.this, TankDrive.class);
            leftPower = addInput("leftPower", defaultLeftPower);
            rightPower = addInput("rightPower", defaultRightPower);
        }

        @Override
        public void run () {
            robotDrive.tankDrive(leftPower.get(), rightPower.get());
        }
    }

    public class ArcadeDrive extends Action {
        public final Input<Double> movePower;
        public final Input<Double> rotatePower;

        public ArcadeDrive () {
            this(0, 0);
        }

        public ArcadeDrive (double defaultLeftPower, double defaultRightPower) {
            super(Drive.this, ArcadeDrive.class);
            movePower = addInput("movePower", 0d);
            rotatePower = addInput("rotatePower", 0d);
        }

        @Override
        public void run () {
            robotDrive.arcadeDrive(movePower.get(), rotatePower.get());
        }
    }

    public Drive () {
        super(Drive.class);
        setDefaultAction(idle);
    }
}