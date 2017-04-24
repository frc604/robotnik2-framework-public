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
            if (driver.buttons.LT.get()) {
                climb.power.set(driver.triggers.Left.get());
                climb.activate();
            }
        }
    }

    private class SignalLightController {
        private final Toggle lightToggle = new Toggle(false);

        public void run () {
            lightToggle.update(driver.buttons.RT.get());
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
            if (driver.buttons.Y.get()) {
                extend = false;
            }
            if (driver.buttons.X.get() || driver.buttons.B.get() || driver.buttons.A.get()) {
                extend = true;
            }

            if (extend) {
                robot.flipFlop.extend.activate();
            }

            if (driver.buttons.Y.get()) {
                intakeState = IntakeState.IDLE;
            }
            if (driver.buttons.X.get()) {
                intakeState = IntakeState.FORWARD;
            }
            if (driver.buttons.B.get()) {
                intakeState = IntakeState.REVERSE;
            }

            switch (intakeState) {
                case IDLE:
                case FORWARD:

                    break;
            }
        }
    }
}