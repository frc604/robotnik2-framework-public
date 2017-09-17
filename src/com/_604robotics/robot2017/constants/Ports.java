package com._604robotics.robot2017.constants;

public class Ports {
    private Ports () {}
    
    // Motors
	// check this
    public static final int DRIVE_FRONT_LEFT_MOTOR = 7;
    public static final int DRIVE_REAR_LEFT_MOTOR = 5;
    public static final int DRIVE_FRONT_RIGHT_MOTOR = 0; // 0 OR 9 reverse
    public static final int DRIVE_REAR_RIGHT_MOTOR = 9; // 0 OR 9 reverse
	public static final int CLIMBER_MOTOR = 8;																																																																																				
    public static final int INTAKE_LEFT_MOTOR = 4;
    public static final int INTAKE_RIGHT_MOTOR = 6;
  
    // Shooter
    public static final int WHEEL_MID = 2;
    public static final int WHEEL_BOT = 3;
    public static final int BELT = 1; // not plugged in
    
    // Relays
	// AZ change these to real
    public static final int SIGNAL_LIGHT_RELAY = 2;

    // Solenoids
    // check these: one of 0, 6
    public static final int SHIFTER_FORWARD = 0;
	public static final int SHIFTER_REVERSE = 6;
	// 4 and 7: may switch
    public static final int FLIP_FLOP_EXTEND_SOLENOID = 4;
    public static final int FLIP_FLOP_RETRACT_SOLENOID = 7;

    // Digital Inputs
    public static final int ENCODER_LEFT_A = 2;
    public static final int ENCODER_LEFT_B = 3;
    public static final int ENCODER_RIGHT_A = 0;
    public static final int ENCODER_RIGHT_B = 1;
    
    // check these
    public static final int INTAKE_LEFT_SENSOR_DIGITAL_INPUT = 4;
    public static final int INTAKE_RIGHT_SENSOR_DIGITAL_INPUT = 5;
   
    // Analog
    public static final int HORIZGYRO = 0;
    
    
    // CAN
    public static final int COMPRESSOR = 0;
    public static final int PDP_MODULE = 1;

    public static final int WHEEL_TOP_A = 19;
    public static final int WHEEL_TOP_B = 17;
}