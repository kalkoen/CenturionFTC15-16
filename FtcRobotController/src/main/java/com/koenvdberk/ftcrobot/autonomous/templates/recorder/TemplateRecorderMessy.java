package com.koenvdberk.ftcrobot.autonomous.templates.recorder;

import com.koenvdberk.ftcrobot.autonomous.templates.FileTemplate;
import com.koenvdberk.ftcrobot.drivercontrolled.Driver;
import com.koenvdberk.ftcrobot.drivercontrolled.MotionController;
import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.koenvdberk.ftcrobot.input.GamepadListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TemplateRecorderMessy extends OpMode {

    private RobotHardware robotHardware;

    private Driver driver;
    private MotionController motionController;

    private GamepadListener gamepadListener1;

    private StringBuilder output;

    private long previousUpdateTime;

    private int saveIndex;

    @Override
    public void init() {
        setRobotHardware(new RobotHardware(hardwareMap));

        setDriver(getRobotHardware().getNewDriver());
        setMotionController(getRobotHardware().getNewMotionController());

        setGamepadListener1(new GamepadListener(gamepad1));

        setOutput(new StringBuilder());

        setPreviousUpdateTime(-1);
        setSaveIndex(0);

        registerListeners();
    }

    private void registerListeners() {
        getGamepadListener1().registerListener(new GamepadListener.InputListener() {
            @Override
            public Object getValue(Gamepad gamepad) {
                return gamepad.dpad_up;
            }

            @Override
            public void onChange() {
                if (gamepad1.dpad_up) {
                    setSaveIndex(getSaveIndex() + 1);
                }
            }
        });

        getGamepadListener1().registerListener(new GamepadListener.InputListener() {
            @Override
            public Object getValue(Gamepad gamepad) {
                return gamepad.dpad_down;
            }

            @Override
            public void onChange() {
                if (gamepad1.dpad_down) {
                    setSaveIndex(Math.max(getSaveIndex() - 1, 0));
                }
            }
        });

    }

    @Override
    public void loop() {
        telemetry.addData("Save index", String.valueOf(getSaveIndex()));

        gamepad1.left_stick_x = gamepad1.left_stick_x > 0 ? 1 : (gamepad1.left_stick_x < 0 ? -1 : 0);
        gamepad1.left_stick_y = gamepad1.left_stick_y > 0 ? 1 : (gamepad1.left_stick_y < 0 ? -1 : 0);
        gamepad1.left_trigger = gamepad1.left_trigger > 0 ? 1 : (gamepad1.left_trigger < 0 ? -1 : 0);
        gamepad1.right_trigger = gamepad1.right_trigger > 0 ? 1 : (gamepad1.right_trigger < 0 ? -1 : 0);

        getGamepadListener1().update();

        double frontLeftPower = getRobotHardware().getFrontLeftMotor().getPower();
        double frontRightPower = getRobotHardware().getFrontRightMotor().getPower();
        double backLeftPower = getRobotHardware().getBackLeftMotor().getPower();
        double backRightPower = getRobotHardware().getBackRightMotor().getPower();
        double catapultServoPosition = getRobotHardware().getCatapultServo().getPosition();
        double pusherServoPosition = getRobotHardware().getPusherServo().getPosition();

        getDriver().drive(gamepad1);
        getMotionController().act(gamepad2);

        double frontLeftPower2 = getRobotHardware().getFrontLeftMotor().getPower();
        double frontRightPower2 = getRobotHardware().getFrontRightMotor().getPower();
        double backLeftPower2 = getRobotHardware().getBackLeftMotor().getPower();
        double backRightPower2 = getRobotHardware().getBackRightMotor().getPower();
        double catapultServoPosition2 = getRobotHardware().getCatapultServo().getPosition();
        double pusherServoPosition2 = getRobotHardware().getPusherServo().getPosition();

        String addToOutput = "";
        if(frontLeftPower != frontLeftPower2) {
            addToOutput += "MP " + frontLeftPower2 + " " + RobotHardware.FRONT_LEFT_MOTOR_NAME + "\n";
        }
        if(backLeftPower != backLeftPower2) {
            addToOutput += "MP " + backLeftPower2 + " " + RobotHardware.BACK_LEFT_MOTOR_NAME + "\n";
        }
        if(frontRightPower != frontRightPower2) {
            addToOutput += "MP " + frontRightPower2 + " " + RobotHardware.FRONT_RIGHT_MOTOR_NAME + "\n";
        }
        if(backRightPower != backRightPower2) {
            addToOutput += "MP " + backRightPower2 + " " + RobotHardware.BACK_RIGHT_MOTOR_NAME + "\n";
        }
        if(catapultServoPosition != catapultServoPosition2) {
            addToOutput += "SP " + catapultServoPosition2 + " " + RobotHardware.CATAPULT_SERVO_NAME + "\n";
        }
        if(pusherServoPosition != pusherServoPosition2) {
            addToOutput += "SP " + pusherServoPosition2 + " " + RobotHardware.PUSHER_SERVO_NAME + "\n";
        }
        if(!addToOutput.isEmpty()) {
            if(getPreviousUpdateTime() != -1) {
                long deltaTime = System.currentTimeMillis() - getPreviousUpdateTime();
                output.append("SLEEP ").append(deltaTime).append("\n");
            }
            output.append(addToOutput);
            setPreviousUpdateTime(System.currentTimeMillis());
        }
    }

    @Override
    public void stop() {
        FileTemplate fileTemplate = new FileTemplate(String.valueOf(getSaveIndex()));
        try {
            PrintWriter writer = new PrintWriter(fileTemplate.getFile());
            writer.write(getOutput().toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getDriver().stop();
        getMotionController().stop();
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

    public GamepadListener getGamepadListener1() {
        return gamepadListener1;
    }

    public void setGamepadListener1(GamepadListener gamepadListener1) {
        this.gamepadListener1 = gamepadListener1;
    }

    public StringBuilder getOutput() {
        return output;
    }

    public void setOutput(StringBuilder output) {
        this.output = output;
    }

    public long getPreviousUpdateTime() {
        return previousUpdateTime;
    }

    public void setPreviousUpdateTime(long previousUpdateTime) {
        this.previousUpdateTime = previousUpdateTime;
    }

    public int getSaveIndex() {
        return saveIndex;
    }

    public void setSaveIndex(int saveIndex) {
        this.saveIndex = saveIndex;
    }
}
