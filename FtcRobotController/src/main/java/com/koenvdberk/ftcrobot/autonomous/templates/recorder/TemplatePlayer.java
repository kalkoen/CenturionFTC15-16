package com.koenvdberk.ftcrobot.autonomous.templates.recorder;

import com.koenvdberk.ftcrobot.autonomous.AutonomousScheduler;
import com.koenvdberk.ftcrobot.autonomous.templates.FileTemplate;
import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.koenvdberk.ftcrobot.input.GamepadListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

public class TemplatePlayer extends OpMode {

    public static final int INDEX = 17;

    private AutonomousScheduler autonomousScheduler;
    private RobotHardware robotHardware;

    private GamepadListener gamepadListener1;

    private boolean schedulerRunning, templateLoaded;
    private int loadIndex;

    @Override
    public void init() {
        setAutonomousScheduler(new AutonomousScheduler());
        setRobotHardware(new RobotHardware(hardwareMap));

        setGamepadListener1(new GamepadListener(gamepad1));

        setSchedulerRunning(false);
        setTemplateLoaded(false);
        setLoadIndex(0);

        setLoadIndex(INDEX);

        getAutonomousScheduler().getActions().clear();
        FileTemplate fileTemplate = new FileTemplate(String.valueOf(getLoadIndex()));
        fileTemplate.addActions(getAutonomousScheduler(), hardwareMap);
        setTemplateLoaded(true);

        setSchedulerRunning(true);

        //registerListeners();
    }

    public void registerListeners() {
        getGamepadListener1().registerListener(new GamepadListener.InputListener() {
            @Override
            public Object getValue(Gamepad gamepad) {
                return gamepad.b;
            }

            @Override
            public void onChange() {
                if (gamepad1.b && !isTemplateLoaded()) {
                    getAutonomousScheduler().getActions().clear();
                    FileTemplate fileTemplate = new FileTemplate(String.valueOf(getLoadIndex()));
                    fileTemplate.addActions(getAutonomousScheduler(), hardwareMap);
                    setTemplateLoaded(true);
                }
            }
        });

        getGamepadListener1().registerListener(new GamepadListener.InputListener() {
            @Override
            public Object getValue(Gamepad gamepad) {
                return gamepad.a;
            }

            @Override
            public void onChange() {
                if(gamepad1.a && isTemplateLoaded()) {
                    setSchedulerRunning(!isSchedulerRunning());
                }
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
                    setLoadIndex(getLoadIndex() + 1);
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
                    setLoadIndex(Math.max(getLoadIndex() - 1, 0));
                }
            }
        });
    }

    @Override
    public void loop() {
        //getGamepadListener1().update();

        telemetry.addData("Status", isSchedulerRunning() ? "running" : isTemplateLoaded() ? "waiting for start" : "waiting to load template");

        if(isSchedulerRunning()) {
            telemetry.addData("Remaining actions", String.valueOf(getAutonomousScheduler().getActions().size()));
            getAutonomousScheduler().run();
        } else {
            telemetry.addData("Load index", String.valueOf(getLoadIndex()));
        }

    }

    @Override
    public void stop() {
        getRobotHardware().resetMotors();
    }

    public AutonomousScheduler getAutonomousScheduler() {
        return autonomousScheduler;
    }

    public void setAutonomousScheduler(AutonomousScheduler autonomousScheduler) {
        this.autonomousScheduler = autonomousScheduler;
    }

    public RobotHardware getRobotHardware() {
        return robotHardware;
    }

    public void setRobotHardware(RobotHardware robotHardware) {
        this.robotHardware = robotHardware;
    }

    public GamepadListener getGamepadListener1() {
        return gamepadListener1;
    }

    public void setGamepadListener1(GamepadListener gamepadListener1) {
        this.gamepadListener1 = gamepadListener1;
    }

    public boolean isSchedulerRunning() {
        return schedulerRunning;
    }

    public void setSchedulerRunning(boolean schedulerRunning) {
        this.schedulerRunning = schedulerRunning;
    }

    public boolean isTemplateLoaded() {
        return templateLoaded;
    }

    public void setTemplateLoaded(boolean templateLoaded) {
        this.templateLoaded = templateLoaded;
    }

    public int getLoadIndex() {
        return loadIndex;
    }

    public void setLoadIndex(int loadIndex) {
        this.loadIndex = loadIndex;
    }
}
