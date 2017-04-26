package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Climber;
import com._604robotics.robotnik.Controller;
import com._604robotics.robotnik.prefabs.controller.xbox.XboxController;
import com._604robotics.robotnik.prefabs.flow.Toggle;

public class TeleopMode extends Controller {
    public static final XboxController driver = new XboxController(0);

    private final Robot2017 robot;

    private final ClimberController climberController;
    private final SignalLightController signalLightController;
    private final PickupController pickupController;

    public TeleopMode (Robot2017 robot) {
        this.robot = robot;

        climberController = new ClimberController();
        signalLightController = new SignalLightController();
        pickupController = new PickupController();
    }

    @Override
    public void run () {
        climberController.run();
        signalLightController.run();
        pickupController.run();
    }

    private class ClimberController {
        private final Climber.Climb climb;

        public ClimberController () {
            climb = robot.climber.new Climb();
        }

        public void run () {
            if (driver.buttons.lt.get()) {
                climb.power.set(driver.triggers.left.get());
                climb.activate();
            }
        }
    }

    private class SignalLightController {
        private final Toggle lightToggle = new Toggle(false);

        public void run () {
            lightToggle.update(driver.buttons.rt.get());
            if (lightToggle.get()) {
                robot.signalLight.on.activate();
            }
        }
    }

    private enum IntakeState {
        IDLE, FORWARD, REVERSE
    }

    private class PickupController {
        private boolean extend = false;
        private IntakeState intakeState = IntakeState.IDLE;

        public void run () {
            if (driver.buttons.y.get()) {
                extend = false;
            }
            if (driver.buttons.x.get() || driver.buttons.b.get() || driver.buttons.a.get()) {
                extend = true;
            }

            if (extend) {
                robot.flipFlop.extend.activate();
            }

            if (driver.buttons.y.get()) {
                intakeState = IntakeState.IDLE;
            }
            if (driver.buttons.x.get()) {
                intakeState = IntakeState.FORWARD;
            }
            if (driver.buttons.b.get()) {
                intakeState = IntakeState.REVERSE;
            }

            switch (intakeState) {
                case FORWARD:
                    // TODO: Fill me in!
                    break;
                case REVERSE:
                    // TODO: Fill me in!
                    break;
            }
        }
    }
}