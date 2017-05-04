package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Climber;
import com._604robotics.robot2017.modules.Drive;
import com._604robotics.robot2017.modules.Shooter;
import com._604robotics.robotnik.Coordinator;
import com._604robotics.robotnik.prefabs.controller.xbox.XboxController;
import com._604robotics.robotnik.prefabs.flow.Toggle;

public class TeleopMode extends Coordinator {
    private final XboxController driver = new XboxController(0);

    private final Robot2017 robot;

    private final ClimberManager climberManager;
    private final SignalLightManager signalLightManager;
    private final PickupManager pickupManager;
    private final DriveManager driveManager;
    private final ShooterManager shootManager;

    public TeleopMode (Robot2017 robot) {
        this.robot = robot;

        climberManager = new ClimberManager();
        signalLightManager = new SignalLightManager();
        pickupManager = new PickupManager();
        driveManager = new DriveManager();
        shootManager = new ShooterManager();
    }

    @Override
    public boolean run () {
        climberManager.run();
        signalLightManager.run();
        pickupManager.run();
        driveManager.run();
        shootManager.run();
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


    private class ShooterManager {
        private final Shooter.ShootAction shoot;

        public ShooterManager () {
            shoot = robot.shooter.new ShootAction();
        }

        public void run () {
            // TODO: REBIND
            if (false) {
                shoot.activate();
            }
        }
    }

    private enum CurrentDrive {
        IDLE, ARCADE, TANK
    }

    private class DriveManager {
        private final Drive.ArcadeDrive arcade;
        private final Drive.TankDrive tank;
        private final Drive.Idle idle;
        private CurrentDrive currentDrive;
        private Toggle inverted;

        public DriveManager () {
            idle=robot.drive.new Idle();
            arcade=robot.drive.new ArcadeDrive();
            tank=robot.drive.new TankDrive();
            // TODO: Expose on dashboard
            currentDrive=CurrentDrive.IDLE;
            // TODO: Expose on dashboard
            inverted=new Toggle(false);
        }

        public void run() {
            double leftY=driver.leftStick.y.get();
            double rightX=driver.rightStick.x.get();
            double rightY=driver.rightStick.y.get();
            // Flip values if xbox inverted
            inverted.update(driver.buttons.rb.get());
            if (inverted.isInOnState()) {
                leftY*=-1;
                rightY*=-1;
            }
            // Get Dashboard option for drive
            switch (robot.dashboard.driveMode.get()){
                case OFF:
                    currentDrive=CurrentDrive.IDLE;
                    break;
                case ARCADE:
                    currentDrive=CurrentDrive.ARCADE;
                    break;
                case TANK:
                    currentDrive=CurrentDrive.TANK;
                    break;
                case DYNAMIC:
                    // Dynamic Drive mode detection logic
                    if (currentDrive == CurrentDrive.TANK) {
                        if (Math.abs(rightY) <= 0.2 && Math.abs(rightX) > 0.3) {
                            currentDrive = CurrentDrive.ARCADE;
                        }
                    } else { // currentDrive == CurrentDrive.ARCADE
                        if (Math.abs(rightX) <= 0.2 && Math.abs(rightY) > 0.3) {
                            currentDrive = CurrentDrive.TANK;
                        }
                    }
                    break;
            }

            // Set appropriate drive mode depending on dashboard option
            switch (currentDrive) {
                case IDLE:
                    idle.activate();
                    break;
                case ARCADE:
                    arcade.movePower.set(leftY);
                    arcade.rotatePower.set(rightX);
                    arcade.activate();
                    break;
                case TANK:
                    tank.leftPower.set(leftY);
                    tank.rightPower.set(rightY);
                    tank.activate();
                    break;
            }

        }
    }

    private class SignalLightManager {
        private final Toggle lightToggle = new Toggle(false);

        public void run () {
            lightToggle.update(driver.buttons.rt.get());
            if (lightToggle.isInOnState()) {
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
                // TODO: Check that I bound the right enum state to the right action
                case FORWARD:
                    robot.intake.spit.activate();
                    break;
                case REVERSE:
                    robot.intake.suck.activate();
                    break;
                case IDLE:
                    robot.intake.idle.activate();
                    break;
            }
        }
    }
}
