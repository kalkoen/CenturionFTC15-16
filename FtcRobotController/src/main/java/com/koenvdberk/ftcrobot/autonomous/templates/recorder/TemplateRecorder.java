package com.koenvdberk.ftcrobot.autonomous.templates.recorder;

import com.koenvdberk.ftcrobot.ChangeListener;
import com.koenvdberk.ftcrobot.autonomous.templates.FileTemplate;
import com.koenvdberk.ftcrobot.drivercontrolled.Driver;
import com.koenvdberk.ftcrobot.drivercontrolled.MotionController;
import com.koenvdberk.ftcrobot.hardware.HardwareListener;
import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.koenvdberk.ftcrobot.input.GamepadListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TemplateRecorder extends OpMode {

    private RobotHardware robotHardware;

    private Driver driver;
    private MotionController motionController;

    private GamepadListener gamepadListener1;

    private HardwareListener.DcMotorListener motorListener;
    private HardwareListener.ServoListener servoListener;

    private StringBuilder iterationOutput;
    private StringBuilder output;

    private long previousUpdateTime;

    private int saveIndex;

    @Override
    public void init() {
        setRobotHardware(new RobotHardware(hardwareMap));

        setDriver(getRobotHardware().getNewDriver());
        setMotionController(getRobotHardware().getNewMotionController());

        setGamepadListener1(new GamepadListener(gamepad1));

        setMotorListener(new HardwareListener.DcMotorListener());
        setServoListener(new HardwareListener.ServoListener());

        setOutput(new StringBuilder());
        setIterationOutput(new StringBuilder());

        setPreviousUpdateTime(-1);
        setSaveIndex(0);

        registerListeners();
    }

    private void registerListeners() {
        getMotorListener().registerListener(getRobotHardware().getBackLeftMotor(), new ChangeListener() {
            @Override
            public void onChange() {
                getIterationOutput()
                        .append("MP ")
                        .append(getRobotHardware().getBackLeftMotor().getPower())
                        .append(" ")
                        .append(RobotHardware.BACK_LEFT_MOTOR_NAME)
                        .append(",")
                        .append(RobotHardware.FRONT_LEFT_MOTOR_NAME)
                        .append("\n");
            }
        });

        getMotorListener().registerListener(getRobotHardware().getBackRightMotor(), new ChangeListener() {
            @Override
            public void onChange() {
                getIterationOutput()
                        .append("MP ")
                        .append(getRobotHardware().getBackRightMotor().getPower())
                        .append(" ")
                        .append(RobotHardware.BACK_RIGHT_MOTOR_NAME)
                        .append(",")
                        .append(RobotHardware.FRONT_RIGHT_MOTOR_NAME)
                        .append("\n");
            }
        });

        getServoListener().registerListener(getRobotHardware().getCatapultServo(), new ChangeListener() {
            @Override
            public void onChange() {
                getIterationOutput()
                        .append("SP ")
                        .append(getRobotHardware().getCatapultServo().getPosition())
                        .append(" ")
                        .append(RobotHardware.CATAPULT_SERVO_NAME)
                        .append("\n");
            }
        });

        getServoListener().registerListener(getRobotHardware().getPusherServo(), new ChangeListener() {
            @Override
            public void onChange() {
                getIterationOutput()
                        .append("SP ")
                        .append(getRobotHardware().getPusherServo().getPosition())
                        .append(" ")
                        .append(RobotHardware.PUSHER_SERVO_NAME)
                        .append("\n");
            }
        });


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

        getDriver().drive(gamepad1);
        getMotionController().act(gamepad2);

        getMotorListener().update();
        getServoListener().update();

        writeToOutput();
    }

    private void writeToOutput() {
        if(getIterationOutput().length() > 0) {
            if(getPreviousUpdateTime() != -1) {
                long timeDifference = System.currentTimeMillis() - getPreviousUpdateTime();
                getOutput()
                        .append("SLEEP ")
                        .append(timeDifference)
                        .append("\n");
            }

            getOutput().append(getIterationOutput());

            setIterationOutput(new StringBuilder());

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

    public HardwareListener.DcMotorListener getMotorListener() {
        return motorListener;
    }

    public void setMotorListener(HardwareListener.DcMotorListener motorListener) {
        this.motorListener = motorListener;
    }

    public HardwareListener.ServoListener getServoListener() {
        return servoListener;
    }

    public void setServoListener(HardwareListener.ServoListener servoListener) {
        this.servoListener = servoListener;
    }

    public StringBuilder getIterationOutput() {
        return iterationOutput;
    }

    public void setIterationOutput(StringBuilder iterationOutput) {
        this.iterationOutput = iterationOutput;
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
