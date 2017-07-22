package com._604robotics.robot2017.systems;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robotnik.Coordinator;
import com._604robotics.robotnik.prefabs.modules.PowerMonitor;

public class DashboardSystem extends Coordinator {
    private final Robot2017 robot;

    public DashboardSystem (Robot2017 robot) {
        this.robot = robot;
    }

    @Override
    public boolean run () {
        robot.intake.power.set(robot.dashboard.intakePower.get());
        robot.flipFlop.transitionTime.set(robot.dashboard.flipFlopTransitionTime.get());
        robot.shooter.threshold.set(robot.dashboard.shooterTarget.get());
        robot.dashboard.leftDriveClicks.set(robot.drive.leftClicks.get());
        robot.dashboard.rightDriveClicks.set(robot.drive.rightClicks.get());
        robot.dashboard.leftDriveRate.set(robot.drive.leftClickRate.get());
        robot.dashboard.rightDriveRate.set(robot.drive.rightClickRate.get());
        robot.dashboard.gyroAngle.set(robot.drive.gyroAngle.get());
        robot.dashboard.totalCurrent.set(PowerMonitor.totalCurrent.get());
        return true;
    }
}
