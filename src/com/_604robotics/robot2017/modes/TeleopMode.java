package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Climber;
import com._604robotics.robotnik.Coordinator;
import com._604robotics.robotnik.prefabs.controller.xbox.XboxController;
import com._604robotics.robotnik.prefabs.flow.Toggle;

public class TeleopMode extends Coordinator {
    public static final XboxController driver = new XboxController(0);

    private final Robot2017 robot;

    private final ClimberManager climberManager;
    private final SignalLightManager signalLightManager;
    private final PickupManager pickupManager;

    public TeleopMode (Robot2017 robot) {
        this.robot = robot;

        climberManager = new ClimberManager();
        signalLightManager = new SignalLightManager();
        pickupManager = new PickupManager();
    }

    @Override
    public boolean run () {
        climberManager.run();
        signalLightManager.run();
        pickupManager.run();
        return true;
    }

    private class ClimberManager {
        private final Climber.Climb climb;

        public ClimberManager () {
            climb = robot.climber.new Climb();
        }

        public void run () {
            if (driver.buttons.lt.get()) {
                climb.power.set(driver.triggers.left.get());
                climb.activate();
            }
        }
    }

    private class SignalLightManager {
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

    private class PickupManager {
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
