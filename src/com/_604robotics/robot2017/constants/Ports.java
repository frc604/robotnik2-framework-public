package com._604robotics.robot2017.constants;

public class Ports {
    private Ports () {}

	public static final int HORIZGYRO = 0;
    
    // Motors
    public static final int CLIMBER_MOTOR = 4;
    public static final int INTAKE_LEFT_MOTOR = 5;

    public static final int DRIVE_FRONT_LEFT_MOTOR = 13;//0
    public static final int DRIVE_REAR_LEFT_MOTOR = 14;//1
    public static final int DRIVE_FRONT_RIGHT_MOTOR = 15;//2
    public static final int DRIVE_REAR_RIGHT_MOTOR = 16;//3

    public static final int INTAKE_RIGHT_MOTOR = 6;

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
    
    public static final int ENCODER_SHOOTER_TOP_A = 4;
    public static final int ENCODER_SHOOTER_TOP_B = 5;
    
    public static final int ENCODER_SHOOTER_MID_A = 6;
    public static final int ENCODER_SHOOTER_MID_B = 7;
    
    
    // Shooter
    // TODO: theoretical
    public static final int WHEEL_TOP_A = 2; // was 7
    public static final int WHEEL_TOP_B = 3; // was 8
    
    public static final int WHEEL_MID = 1; // was 9
    
    public static final int WHEEL_BOT = 0; // was 10
    public static final int BELT = 11;
    
    // Misc
    public static final int PDP_MODULE = 1;

    public static final int COMPRESSOR = 0;
}