package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.macros.TimedDriveMacro;
import com._604robotics.robotnik.Coordinator;
import com._604robotics.robotnik.prefabs.coordinators.StatefulCoordinator;

public class AutonomousMode extends Coordinator {
    private final Robot2017 robot;

    private final Coordinator middleAutonModeMacro;

    private final Coordinator blueLeftAutonModeMacro;
    private final Coordinator blueRightAutonModeMacro;

    private final Coordinator redLeftAutonModeMacro;
    private final Coordinator redRightAutonModeMacro;

    private Coordinator selectedModeMacro;

    public AutonomousMode (Robot2017 robot) {
        this.robot = robot;

        middleAutonModeMacro = new MiddleAutonModeMacro();

        blueLeftAutonModeMacro = new SideAutonModeMacro("BlueLeftAutonModeMacro") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.blueLeftAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.blueLeftAutonTurnToFacePegAngle.get(); }
        };

        blueRightAutonModeMacro = new SideAutonModeMacro("BlueRightAutonModeMacro") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.blueRightAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.blueRightAutonTurnToFacePegAngle.get(); }
        };

        redLeftAutonModeMacro = new SideAutonModeMacro("RedLeftAutonModeMacro") {
            @Override
            protected double getDriveForwardClicks () { return robot.dashboard.redLeftAutonDriveForwardClicks.get(); }
            @Override
            protected double getTurnToFacePegAngle () { return robot.dashboard.redLeftAutonTurnToFacePegAngle.get(); }
        };

        redRightAutonModeMacro = new SideAutonModeMacro("RedRightAutonModeMacro") {
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
                selectedModeMacro = middleAutonModeMacro;
                break;
            case BLUE_LEFT:
                selectedModeMacro = blueLeftAutonModeMacro;
                break;
            case BLUE_RIGHT:
                selectedModeMacro = blueRightAutonModeMacro;
                break;
            case RED_LEFT:
                selectedModeMacro = redLeftAutonModeMacro;
                break;
            case RED_RIGHT:
                selectedModeMacro = redRightAutonModeMacro;
                break;
            default:
                selectedModeMacro = null;
                break;
        }

        if (selectedModeMacro != null) {
            selectedModeMacro.start();
        }
    }

    @Override
    public boolean run () {
        if (selectedModeMacro == null) {
            return false;
        }

        return selectedModeMacro.execute();
    }

    @Override
    public void end () {
        if (selectedModeMacro != null) {
            selectedModeMacro.stop();
        }
    }

    private class MiddleAutonModeMacro extends StatefulCoordinator {
        public MiddleAutonModeMacro () {
            super(MiddleAutonModeMacro.class);

            addState("driveForward", new TimedDriveMacro(robot) {
                @Override
                protected double getLeftPower () { return robot.dashboard.middleAutonDriveForwardLeftPower.get(); }
                @Override
                protected double getRightPower () { return robot.dashboard.middleAutonDriveForwardRightPower.get(); }
                @Override
                protected double getTime () { return robot.dashboard.middleAutonDriveForwardTime.get(); }
            });
        }
    }

    private abstract class SideAutonModeMacro extends StatefulCoordinator {
        public SideAutonModeMacro (String name) {
            super(name);

            addState("driveForward", new Coordinator() {
                // TODO: Fill me in!
            });

            addState("turnToFacePeg", new Coordinator() {
                // TODO: Fill me in!
            });

            addState("driveToPeg", new TimedDriveMacro(robot) {
                @Override
                protected double getLeftPower () { return robot.dashboard.sideAutonDriveToPegLeftPower.get(); }
                @Override
                protected double getRightPower () { return robot.dashboard.sideAutonDriveToPegRightPower.get(); }
                @Override
                protected double getTime () { return robot.dashboard.sideAutonDriveToPegTime.get(); }
            });
        }

        protected abstract double getDriveForwardClicks ();
        protected abstract double getTurnToFacePegAngle ();
    }
}