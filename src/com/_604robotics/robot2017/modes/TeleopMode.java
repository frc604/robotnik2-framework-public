package com._604robotics.robot2017.modes;

import com._604robotics.robot2017.Robot2017;
import com._604robotics.robot2017.modules.Drive;
import com._604robotics.robotnik.Controller;
import com._604robotics.robotnik.prefabs.controller.xbox.XboxController;

public class TeleopMode extends Controller {
    public static final XboxController driver = new XboxController(0);

    private final Robot2017 robot;

    private final DriveController driveController;
    
    public TeleopMode (Robot2017 robot) {
        this.robot = robot;

        driveController = new DriveController();
    }
    
    @Override
    public void run () {
        driveController.run();
    }
    
    private class DriveController {
        private final Drive.TankDrive tankDrive;
        
        public DriveController () {
            tankDrive = robot.drive.tankDrive();
        }

        public void run () {
            tankDrive.leftPower.set(driver.leftStick.Y.get());
            tankDrive.rightPower.set(driver.rightStick.Y.get());
            tankDrive.activate();
        }
    }
}