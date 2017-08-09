package com.koenvdberk.ftcrobot.autonomous.modes;

import com.koenvdberk.ftcrobot.autonomous.AutonomousScheduler;
import com.koenvdberk.ftcrobot.drivercontrolled.Driver;
import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Deprecated
public class AutonomousBot extends OpMode {

    AutonomousScheduler scheduler;
    RobotHardware robotHardware;

    Driver driver;

    @Override
    public void init() {
        scheduler = new AutonomousScheduler();
        robotHardware = new RobotHardware(hardwareMap);

        driver = robotHardware.getNewDriver();
    }

    @Override
    public void loop() {

        driver.drive(gamepad1);

        if(gamepad1.dpad_left) {
            robotHardware.getServoController().setServoPosition(1, 0);
        } else if(gamepad1.dpad_right) {
            robotHardware.getServoController().setServoPosition(1, 1);
        }

        if(gamepad1.dpad_up) {
            robotHardware.getServoController().setServoPosition(2, 0);
        } else if(gamepad1.dpad_down) {
            robotHardware.getServoController().setServoPosition(2, 1);
        }

//        scheduler.run();

    }

    @Override
    public void stop() {
        super.stop();
        driver.stop();
    }

}
