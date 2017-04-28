package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Drive;
import com._604robotics.robotnik.Controller;
import com._604robotics.robotnik.prefabs.controllers.StatefulController;
import com._604robotics.robotnik.prefabs.flow.SmartTimer;

public class AutonomousMode extends Controller {
    private final Robot2017 robot;

    private final Controller middleAutonModeController;

    private final Controller blueLeftAutonModeController;
    private final Controller blueRightAutonModeController;

    private final Controller redLeftAutonModeController;
    private final Controller redRightAutonModeController;

    private Controller selectedModeController;

    public AutonomousMode (Robot2017 robot) {
        this.robot = robot;

        middleAutonModeController = new MiddleAutonModeController();

        blueLeftAutonModeController = new SideAutonModeController("BlueLeftAutonModeController") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.blueLeftAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.blueLeftAutonTurnToFacePegAngle.get(); }
        };

        blueRightAutonModeController = new SideAutonModeController("BlueRightAutonModeController") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.blueRightAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.blueRightAutonTurnToFacePegAngle.get(); }
        };

        redLeftAutonModeController = new SideAutonModeController("RedLeftAutonModeController") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.redLeftAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.redLeftAutonTurnToFacePegAngle.get(); }
        };

        redRightAutonModeController = new SideAutonModeController("RedRightAutonModeController") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.redRightAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.redRightAutonTurnToFacePegAngle.get(); }
        };
    }

    @Override
    public void begin () {
        switch (robot.dashboard.autonMode.get()) {
            case MIDDLE:
                selectedModeController = middleAutonModeController;
                break;
            case BLUE_LEFT:
                selectedModeController = blueLeftAutonModeController;
                break;
            case BLUE_RIGHT:
                selectedModeController = blueRightAutonModeController;
                break;
            case RED_LEFT:
                selectedModeController = redLeftAutonModeController;
                break;
            case RED_RIGHT:
                selectedModeController = redRightAutonModeController;
                break;
            default:
                selectedModeController = null;
                break;
        }

        if (selectedModeController != null) {
            selectedModeController.begin();
        }
    }

    @Override
    public void run () {
        if (selectedModeController != null) {
            selectedModeController.run();
        }
    }

    @Override
    public void end () {
        if (selectedModeController != null) {
            selectedModeController.end();
        }
    }

    private class MiddleAutonModeController extends StatefulController {
        public MiddleAutonModeController () {
            super(MiddleAutonModeController.class);

            final State driveForwardState = new TimedDriveState("driveForwardState") {
                @Override
                protected double getLeftPower () { return robot.dashboard.middleAutonDriveForwardLeftPower.get(); }
                @Override
                protected double getRightPower () { return robot.dashboard.middleAutonDriveForwardRightPower.get(); }
                @Override
                protected double getTime () { return robot.dashboard.middleAutonDriveForwardTime.get(); }
                @Override
                protected State getNextState () { return null; }
            };

            setInitialState(driveForwardState);
        }
    }

    private abstract class SideAutonModeController extends StatefulController {
        public SideAutonModeController (String name) {
            super(name);

            final State driveForwardState = new State("driveForwardState") {
                // TODO: Fill me in!
            };

            final State turnToFacePegState = new State("turnToFacePegState") {
                // TODO: Fill me in!
            };

            final State driveToPegState = new TimedDriveState("driveToPegState") {
                @Override
                protected double getLeftPower () { return robot.dashboard.sideAutonDriveToPegLeftPower.get(); }
                @Override
                protected double getRightPower () { return robot.dashboard.sideAutonDriveToPegRightPower.get(); }
                @Override protected double getTime () { return robot.dashboard.sideAutonDriveToPegTime.get(); }
                @Override
                protected State getNextState () { return null; }
            };

            setInitialState(driveForwardState);
        }

        protected abstract double getDriveForwardClicks ();
        protected abstract double getTurnToFacePegAngle ();
    }

    private abstract class TimedDriveState extends StatefulController.State {
        private final Drive.TankDrive driveForward = robot.drive.new TankDrive();
        private final SmartTimer timer = new SmartTimer();

        public TimedDriveState (String name) {
            super(name);
        }

        protected abstract double getLeftPower ();
        protected abstract double getRightPower ();
        protected abstract double getTime ();
        protected abstract StatefulController.State getNextState ();

        @Override
        protected void begin () {
            timer.start();
        }

        @Override
        protected StatefulController.State run () {
            return timer.runUntil(getTime(), getNextState(), () -> {
                driveForward.leftPower.set(getLeftPower());
                driveForward.rightPower.set(getRightPower());
                driveForward.activate();
                return this;
            });
        }

        @Override
        protected void end () {
            timer.stopAndReset();
        }
    };
}