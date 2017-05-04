package com._604robotics.robot2017.modules;

import com._604robotics.robot2017.constants.Calibration;
import com._604robotics.robotnik.Input;
import com._604robotics.robotnik.Output;
import com._604robotics.robotnik.prefabs.modules.DashboardModule;

public class Dashboard extends DashboardModule {
    public final Input<Double> leftDriveClicks = addDashboardInput("leftDriveClicks", 0);
    public final Input<Double> rightDriveClicks = addDashboardInput("rightDriveClicks", 0);

    public final Input<Double> leftDriveRate = addDashboardInput("leftDriveRate", 0);
    public final Input<Double> rightDriveRate = addDashboardInput("rightDriveRate", 0);

    public final Input<Boolean> gyroCalibrated = addDashboardInput("gyroCalibrated", false);
    public final Input<Double> horizontalGyroAngle = addDashboardInput("horizontalGyroAngle", 0);

    public final Input<Boolean> flipFlopExtending = addDashboardInput("flipFlopExtended", false);
    public final Input<Boolean> intakeRunning = addDashboardInput("intakeRunning", false);

    public final Output<Boolean> driveOn = addDashboardOutput("driveOn", true);

    public enum AutonMode {
        OFF,
        MIDDLE,
        BLUE_LEFT,
        BLUE_RIGHT,
        RED_LEFT,
        RED_RIGHT
    }
    
    public enum DriveMode {
        OFF,
        ARCADE,
        TANK,
        DYNAMIC
    }

    public final Output<AutonMode> autonMode = addDashboardOutput("autonMode", AutonMode.OFF, AutonMode.class);
    
    public final Output<DriveMode> driveMode = addDashboardOutput("driveMode", DriveMode.DYNAMIC, DriveMode.class);

    public final Output<Double> intakePower = addDashboardOutput("intakePower", Calibration.INTAKE_POWER);

    public final Output<Double> flipFlopTransitionTime =
            addDashboardOutput("flipFlopTransitionTime", Calibration.FLIP_FLOP_TRANSITION_TIME);

    public final Output<Double> middleAutonDriveForwardLeftPower =
            addDashboardOutput("middleAutonDriveForwardLeftPower", Calibration.MIDDLE_AUTON_DRIVE_FORWARD_LEFT_POWER);
    public final Output<Double> middleAutonDriveForwardRightPower =
            addDashboardOutput("middleAutonDriveForwardRightPower", Calibration.MIDDLE_AUTON_DRIVE_FORWARD_RIGHT_POWER);
    public final Output<Double> middleAutonDriveForwardTime =
            addDashboardOutput("middleAutonDriveForwardTime", Calibration.MIDDLE_AUTON_DRIVE_FORWARD_TIME);

    public final Output<Double> sideAutonDriveToPegLeftPower =
            addDashboardOutput("sideAutonDriveToPegLeftPower", Calibration.SIDE_AUTON_DRIVE_TO_PEG_LEFT_POWER);
    public final Output<Double> sideAutonDriveToPegRightPower =
            addDashboardOutput("sideAutonDriveToPegRightPower", Calibration.SIDE_AUTON_DRIVE_TO_PEG_RIGHT_POWER);
    public final Output<Double> sideAutonDriveToPegTime =
            addDashboardOutput("sideAutonDriveToPegTime", Calibration.SIDE_AUTON_DRIVE_TO_PEG_TIME);

    public final Output<Double> blueLeftAutonDriveForwardClicks =
            addDashboardOutput("blueLeftAutonDriveForwardClicks", Calibration.BLUE_LEFT_AUTON_DRIVE_FORWARD_CLICKS);
    public final Output<Double> blueLeftAutonTurnToFacePegAngle =
            addDashboardOutput("blueLeftAutonTurnToFacePegAngle", Calibration.BLUE_LEFT_AUTON_TURN_TO_FACE_PEG_ANGLE);

    public final Output<Double> blueRightAutonDriveForwardClicks =
            addDashboardOutput("blueRightAutonDriveForwardClicks", Calibration.BLUE_RIGHT_AUTON_DRIVE_FORWARD_CLICKS);
    public final Output<Double> blueRightAutonTurnToFacePegAngle =
            addDashboardOutput("blueRightAutonTurnToFacePegAngle", Calibration.BLUE_RIGHT_AUTON_TURN_TO_FACE_PEG_ANGLE);

    public final Output<Double> redLeftAutonDriveForwardClicks =
            addDashboardOutput("redLeftAutonDriveForwardClicks", Calibration.RED_LEFT_AUTON_DRIVE_FORWARD_CLICKS);
    public final Output<Double> redLeftAutonTurnToFacePegAngle =
            addDashboardOutput("redLeftAutonTurnToFacePegAngle", Calibration.RED_LEFT_AUTON_TURN_TO_FACE_PEG_ANGLE);

    public final Output<Double> redRightAutonDriveForwardClicks =
            addDashboardOutput("redRightAutonDriveForwardClicks", Calibration.RED_RIGHT_AUTON_DRIVE_FORWARD_CLICKS);
    public final Output<Double> redRightAutonTurnToFacePegAngle =
            addDashboardOutput("redRightAutonTurnToFacePegAngle", Calibration.RED_RIGHT_AUTON_TURN_TO_FACE_PEG_ANGLE);
    
    // THEORETICAL
    public final Output<Double> shooterTarget =
            addDashboardOutput("Shooter Target Speed", Calibration.SHOOTER_TARGET);
    public final Input<Double> shooterSpeed =
            addDashboardInput("Shooter Speed", 0);
    public final Input<Double> shooterPower =
            addDashboardInput("Shooter Calc Power", 0);

    public Dashboard () {
        super(Dashboard.class);
    }
}