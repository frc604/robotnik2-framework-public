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
        FAIL_SAFE,
        LEFT,
        MIDDLE,
        RIGHT
    }

    public final Output<AutonMode> autonMode = addDashboardOutput("autonMode", AutonMode.OFF, AutonMode.class);

    public final Output<Double> intakePower = addDashboardOutput("intakePower", Calibration.INTAKE_POWER);

    public final Output<Double> flipFlopTransitionTime =
            addDashboardOutput("flipFlopTransitionTime", Calibration.FLIP_FLOP_TRANSITION_TIME);

    public Dashboard () {
        super(Dashboard.class);
    }
}
