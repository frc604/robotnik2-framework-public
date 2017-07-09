package com._604robotics.robot2017.systems;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robotnik.Coordinator;

public class DashboardSystem extends Coordinator {
    private final Robot2017 robot;

    public DashboardSystem (Robot2017 robot) {
        this.robot = robot;
    }

    @Override
    public boolean run () {
        robot.intake.power.set(robot.dashboard.intakePower.get());
        robot.flipFlop.transitionTime.set(robot.dashboard.flipFlopTransitionTime.get());
        robot.dashboard.leftDriveClicks.set(robot.drive.leftClicks.get());
        robot.dashboard.rightDriveClicks.set(robot.drive.rightClicks.get());
        
        return true;
    }
}