package com.koenvdberk.ftcrobot.drivercontrolled;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

// The op mode used in the driver controlled period.
public class DriverControlledBot extends OpMode {

    private RobotHardware robotHardware;
    private Driver driver;
    private MotionController motionController;

    MediaPlayer polka;

    public DriverControlledBot() {
        super();
    }

    public void init() {
        setRobotHardware(new RobotHardware(hardwareMap));
        setDriver(getRobotHardware().getNewDriver());
        setMotionController(getRobotHardware().getNewMotionController());

        polka = MediaPlayer.create(FtcRobotControllerActivity.context, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/polka.mp3"));
        polka.setLooping(true);
    }

    public void loop() {
        getDriver().drive(gamepad1);
        getMotionController().act(gamepad2);


        if(gamepad1.y) {
            polka.start();
        }

    }

    @Override
    public void stop() {
        super.stop();
        getDriver().stop();
        getMotionController().stop();

        polka.stop();
    }

    public RobotHardware getRobotHardware() {
        return robotHardware;
    }

    public void setRobotHardware(RobotHardware robotHardware) {
        this.robotHardware = robotHardware;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public MotionController getMotionController() {
        return motionController;
    }

    public void setMotionController(MotionController motionController) {
        this.motionController = motionController;
    }
}
