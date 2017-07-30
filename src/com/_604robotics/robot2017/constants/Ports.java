package com._604robotics.robot2017.constants;

public class Ports {
    private Ports () {}

	public static final int HORIZGYRO = 0;
    
    // Motors
    public static final int CLIMBER_MOTOR = 0;
    public static final int INTAKE_LEFT_MOTOR = 1;

    public static final int DRIVE_FRONT_LEFT_MOTOR = 2;
    public static final int DRIVE_REAR_LEFT_MOTOR = 4;
    public static final int DRIVE_FRONT_RIGHT_MOTOR = 7;
    public static final int DRIVE_REAR_RIGHT_MOTOR = 5;

    public static final int INTAKE_RIGHT_MOTOR = 6;
    // THEORETICAL
    public static final int SHOOTER_MOTOR = 8;

    // Relays
    public static final int SIGNAL_LIGHT_RELAY = 0;

    // Solenoids
	public static final int SHIFTER_FORWARD = 0;
	public static final int SHIFTER_REVERSE = 1;
    public static final int FLIP_FLOP_EXTEND_SOLENOID = 6;
    public static final int FLIP_FLOP_RETRACT_SOLENOID = 7;

    // Digital Inputs
    public static final int INTAKE_LEFT_SENSOR_DIGITAL_INPUT = 8;
    public static final int INTAKE_RIGHT_SENSOR_DIGITAL_INPUT = 9;

    // Encoders
    public static final int ENCODER_LEFT_A = 0;
    public static final int ENCODER_LEFT_B = 1;
    public static final int ENCODER_RIGHT_A = 2;
    public static final int ENCODER_RIGHT_B = 3;
    // THEORETICAL
    public static final int ENCODER_SHOOTER_A = 4;
    public static final int ENCODER_SHOOTER_B = 5;
    
    // Misc
    public static final int PDP_MODULE = 1;
}
