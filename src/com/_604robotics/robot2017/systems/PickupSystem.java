package com._604robotics.robot2017.systems;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robotnik.Controller;

public class PickupSystem extends Controller {
    private final Robot2017 robot;

    public PickupSystem (Robot2017 robot) {
        this.robot = robot;
    }

    @Override
    protected void run () {
        if (robot.flipFlop.retract.isRunning() && !robot.flipFlop.retract.completed.get()) {
            robot.intake.suck.activate();
        }
    }
}