package com._604robotics.robot2017.systems;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robotnik.Controller;

public class DashboardSystem extends Controller {
    private final Robot2017 robot;

    public DashboardSystem (Robot2017 robot) {
        this.robot = robot;
    }

    @Override
    public void run () {
        robot.intake.power.set(robot.dashboard.intakePower.get());

        robot.flipFlop.transitionTime.set(robot.dashboard.flipFlopTransitionTime.get());
    }
}