package com._604robotics.robot2017.constants;

public class Calibration {
    private Calibration () {}

    public static final double TELEOP_DEADBAND=0.3;
    public static final double TELEOP_FACTOR=-1;
    public static final double INTAKE_POWER = 1;

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
}