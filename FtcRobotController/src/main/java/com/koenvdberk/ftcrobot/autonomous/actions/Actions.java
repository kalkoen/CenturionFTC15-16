package com.koenvdberk.ftcrobot.autonomous.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public enum Actions {

    MR(new ValueParser() {
        @Override
        public Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names) {
            int rotation = Integer.parseInt(value);
            return motorRotation(rotation, getDcMotors(hardwareMap, names));
        }
    }),

    MAR(new ValueParser() {
        @Override
        public Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names) {
            return motorAtRest(getDcMotors(hardwareMap, names));
        }
    }),

    MP(new ValueParser() {
        @Override
        public Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names) {
            double power = Double.parseDouble(value);
            return motorPower(power, getDcMotors(hardwareMap, names));
        }
    }),

    SP(new ValueParser() {
        @Override
        public Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names) {
            double position = Double.parseDouble(value);
            return servoPosition(position, getServos(hardwareMap, names));
        }
    }),

    SLEEP(new ValueParser() {
        @Override
        public Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names) {
            int time = Integer.parseInt(value);
            return sleep(time);
        }
    });

    private ValueParser valueParser;

    Actions(ValueParser valueParser) {
        setValueParser(valueParser);
    }

    public ValueParser getValueParser() {
        return valueParser;
    }

    public void setValueParser(ValueParser valueParser) {
        this.valueParser = valueParser;
    }

    public static Runnable motorRotation(final int rotation, final DcMotor... motors){
        if(motors.length == 1) {
            final DcMotor motor = motors[0];
            return new Runnable() {
                @Override
                public void run() {
                    motor.setTargetPosition(motor.getCurrentPosition() + rotation);
                }
            };
        } else {
            return new Runnable() {
                @Override
                public void run() {
                    for(DcMotor motor : motors) {
                        motor.setTargetPosition(motor.getCurrentPosition() + rotation);
                    }
                }
            };
        }
    }

    public static Runnable motorAtRest(final DcMotor... motors) {
        if(motors.length == 1) {
            final DcMotor motor = motors[0];
            return new ConditionAction() {
                @Override
                public boolean checkCondition() {
                    return !motor.isBusy();
                }

                @Override
                public boolean onSuccess() {
                    return false;
                }
            };
        } else {
            return new ConditionAction() {
                @Override
                public boolean checkCondition() {
                    for (DcMotor motor : motors) {
                        if (motor.isBusy()) {
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public boolean onSuccess() {
                    return false;
                }
            };
        }
    }

    public static Runnable sleep(int time) {
        return new TimedAction(time);
    }

    public static Runnable motorPower(final double power, final DcMotor... motors) {
        if(motors.length == 1) {
            final DcMotor motor = motors[0];
            return new Runnable() {
                @Override
                public void run() {
                    motor.setPower(power);
                }
            };
        } else {
            return new Runnable() {
                @Override
                public void run() {
                    for (DcMotor motor : motors) {
                        motor.setPower(power);
                    }
                }
            };
        }
    }

    public static Runnable servoPosition(final double position, final Servo... servos) {
        if(servos.length == 1) {
            final Servo servo = servos[0];
            return new Runnable() {
                @Override
                public void run() {
                    servo.setPosition(position);
                }
            };
        } else {
            return new Runnable() {
                @Override
                public void run() {
                    for (Servo servo : servos) {
                        servo.setPosition(position);
                    }
                }
            };
        }
    }


    private static DcMotor[] getDcMotors(HardwareMap hardwareMap, String[] names) {
        DcMotor[] motors = new DcMotor[names.length];
        for (int i = 0; i < motors.length; i++) {
            motors[i] = hardwareMap.dcMotor.get(names[i]);
        }
        return motors;
    }

    private static Servo[] getServos(HardwareMap hardwareMap, String[] names) {
        Servo[] servos = new Servo[names.length];
        for (int i = 0; i < servos.length; i++) {
            servos[i] = hardwareMap.servo.get(names[i]);
        }
        return servos;
    }


    public interface ValueParser {
        Runnable getRunnable(HardwareMap hardwareMap, String value, String[] names);
    }

}
