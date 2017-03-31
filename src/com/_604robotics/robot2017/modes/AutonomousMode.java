package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Drive;
import com._604robotics.robotnik.StatefulController;

import edu.wpi.first.wpilibj.Timer;

public class AutonomousMode extends StatefulController {
    public AutonomousMode (Robot2017 robot) {
        super(AutonomousMode.class.getSimpleName());

        final State driveForwardState = new State("driveForwardState") {
            private final Drive.TankDrive driveForward = robot.drive.tankDrive(0.5, 0.5);
            private final Timer timer = new Timer();
            
            @Override
            protected void begin () {
                timer.start();
            }

            @Override
            protected State run () {
                if (timer.get() < 1) {
                    driveForward.activate();
                    return this;
                } else {
                    return null;
                }
            }
            
            @Override
            protected void end (){
                timer.stop();
                timer.reset();
            }
        };

        setInitialState(driveForwardState);
    }
}