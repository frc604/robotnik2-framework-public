package com._604robotics.robot2017.constants;

import com._604robotics.robotnik.utils.AutonMovement;

public class Calibration {
    private Calibration () {}

    public static final double TELEOP_DEADBAND=0.3;
    public static final double TELEOP_FACTOR=-1;
    public static final double INTAKE_POWER = 1;
    
    public static final double DRIVE_MOVE_PID_P = 0.005;
    public static final double DRIVE_MOVE_PID_I = 0;
    public static final double DRIVE_MOVE_PID_D = 0.01;
    public static final double DRIVE_MOVE_PID_MAX = 0.5;
    public static final double DRIVE_MOVE_TOLERANCE = 20;

    // Rotate PID is now calibrated-don't touch
    public static final double DRIVE_ROTATE_PID_P = 0.01;
    public static final double DRIVE_ROTATE_PID_I = 0;
    public static final double DRIVE_ROTATE_PID_D = 0.018;
    public static final double DRIVE_ROTATE_PID_MAX = 0.3;// was 0.5
    public static final double DRIVE_ROTATE_TOLERANCE = 20;

    public static final double DRIVE_PID_AFTER_TIMING = 1.5;
    public static final double DRIVE_PID_SAMPLE_RATE = 0.01;

    public static final double DRIVE_MOVE_STILL_TARGET = 0;
    public static final double DRIVE_ROTATE_STILL_TARGET = 0;
    
    
    public static final AutonMovement.DriveTrainProperties DRIVE_PROPERTIES
    = new AutonMovement.DriveTrainProperties(490, 29.5, 2, 20.767, 8.323); // second to last = coefficient second value = offset
    static {
    	System.out.println("Clicks over inches is "+DRIVE_PROPERTIES.getClicksOverInches());
    	System.out.println("Clicks over degrees is "+DRIVE_PROPERTIES.getDegreesOverClicks());
    }
    public static final double DRIVE_ROTATE_LEFT_TARGET
    = AutonMovement.degreesToClicks(DRIVE_PROPERTIES, 360);
    public static final double DRIVE_ROTATE_RIGHT_TARGET
    = AutonMovement.degreesToClicks(DRIVE_PROPERTIES, -360);
    public static final double DRIVE_MOVE_FORWARD_TARGET
    = AutonMovement.inchesToClicks(DRIVE_PROPERTIES, 72);
    public static final double DRIVE_MOVE_BACKWARD_TARGET
    = AutonMovement.inchesToClicks(DRIVE_PROPERTIES, -72);
    
    // Most accurate auton mode
    public static final double DRIVE_MOVE_FORWARD_TEST_INCHES = AutonMovement.empericalInchesToClicks(DRIVE_PROPERTIES, 36);
    
    
    public static final double FLIP_FLOP_TRANSITION_TIME = 0.5;

    public static final double MIDDLE_AUTON_DRIVE_FORWARD_LEFT_POWER = 0.881;
    public static final double MIDDLE_AUTON_DRIVE_FORWARD_RIGHT_POWER = 0.889;
    public static final double MIDDLE_AUTON_DRIVE_FORWARD_TIME = 5;

    public static final double SIDE_AUTON_DRIVE_TO_PEG_LEFT_POWER = Math.sqrt(0.5);
    public static final double SIDE_AUTON_DRIVE_TO_PEG_RIGHT_POWER = SIDE_AUTON_DRIVE_TO_PEG_LEFT_POWER;
    public static final double SIDE_AUTON_DRIVE_TO_PEG_TIME = 3;

    public static final double BLUE_LEFT_AUTON_DRIVE_FORWARD_CLICKS = 1980;
    public static final double BLUE_LEFT_AUTON_TURN_TO_FACE_PEG_ANGLE = 0; // TODO: Calibrate!

    public static final double BLUE_RIGHT_AUTON_DRIVE_FORWARD_CLICKS = 1360;
    public static final double BLUE_RIGHT_AUTON_TURN_TO_FACE_PEG_ANGLE = 0; // TODO: Calibrate!

    public static final double RED_LEFT_AUTON_DRIVE_FORWARD_CLICKS = 1300;
    public static final double RED_LEFT_AUTON_TURN_TO_FACE_PEG_ANGLE = 0; // TODO: Calibrate!

    public static final double RED_RIGHT_AUTON_DRIVE_FORWARD_CLICKS = 2000;
    public static final double RED_RIGHT_AUTON_TURN_TO_FACE_PEG_ANGLE = 0; // TODO: Calibrate!
    
    public static final double SHOOTER_TOP_RATE_TARGET = 30000;
    public static final double SHOOTER_MID_RATE_TARGET = 30000;
    public static final double SHOOTER_TOP_RATE_THRESHOLD = 2000;
    public static final double SHOOTER_MID_RATE_THRESHOLD = 2000;
    
    public static final double MIN_CHARGE_TIME = 0.5;
    
    // Negative to have right direction
    public static final double WHEEL_BOT_SPEED = -0.5;
    public static final double BELT_SPEED = 0.5;
    
    public static final double TOP_WHEEL_SPEED_LIMITER = 0.7;
    public static final double MID_WHEEL_SPEED_LIMITER = 0.7;
    
    public static final double MAXIMUM_OVERDRIVE_SPEED = 1;
    
    public static final double AUTON_SHOOTER_INPUT = 1;
    
    public static final double AUTON_SHOOTER_TIME = 5;
}