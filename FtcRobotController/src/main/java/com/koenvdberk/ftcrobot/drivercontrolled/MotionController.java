package com.koenvdberk.ftcrobot.drivercontrolled;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class MotionController {

    // Oh look! Some amazing settings.
    // Who cares anyway. I'm the only one working on this anyway.
    public static final double
            CATAPULT_SERVO_MAX_POS = 0.9,
            CATAPULT_SERVO_MIN_POS = 0,
            PUSHER_SERVO_MAX_POS = 0.4,
            PUSHER_SERVO_MIN_POS = 0,
            PULL_ADJUSTER_SERVO_MIN_POS = 0,
            PULL_ADJUSTER_SERVO_MAX_POS = 1,
            PULL_ADJUSTER_SERVO_SPEED = 0.002,
            PULL_UP_MOTOR_MAX_POWER = 1,
            CONVEYOR_MOTOR_MAX_POWER = 0.5;

//    public static final int PUSHER_SERVO_FALL_TIME = 2000;

//    private long pusherServoFallStartTime;

    private DcMotor pullUpMotor, conveyorMotor;
    private Servo catapultServo, pusherServo, pullAdjusterServo;

    // Initialization bleh
    public MotionController(DcMotor pullUpMotor, DcMotor conveyorMotor, Servo catapultServo, Servo pusherServo, Servo pullAdjusterServo) {
        setPullUpMotor(pullUpMotor);
        setConveyorMotor(conveyorMotor);
        setCatapultServo(catapultServo);
        setPusherServo(pusherServo);
        setPullAdjusterServo(pullAdjusterServo);

//        setPusherServoFallStartTime(-1);

        // To make sure it updates. Don't believe me? Remove it and experience unexpected servo movements!
        getCatapultServo().setPosition(CATAPULT_SERVO_MAX_POS);
        getPusherServo().setPosition(PUSHER_SERVO_MAX_POS);
        getCatapultServo().setPosition(CATAPULT_SERVO_MIN_POS);
        getPusherServo().setPosition(PUSHER_SERVO_MIN_POS);
    }

    public void act(Gamepad gamepad) {
        updateCatapultServo(gamepad);
        updatePusherServo(gamepad);
        updatePullAdjusterServo(gamepad);
        updatePullUpMotor(gamepad);
        updateConveyorMotor(gamepad);
    }

    public void updateCatapultServo(Gamepad gamepad) {
        double catapultServoPosition = getCatapultServo().getPosition();
        if(gamepad.dpad_up) {
            catapultServoPosition = CATAPULT_SERVO_MAX_POS;
        } else if(gamepad.dpad_down) {
            catapultServoPosition = CATAPULT_SERVO_MIN_POS;
        }
        getCatapultServo().setPosition(catapultServoPosition);
    }

    public void updatePusherServo(Gamepad gamepad) {
        //        double pusherServoPosition = getPusherServo().getPosition();
//        if(gamepad.y) {
//            pusherServoPosition = PUSHER_SERVO_MIN_POS;
//            setPusherServoFallStartTime(-1);
//        } else if(gamepad.x && pusherServoPosition == PUSHER_SERVO_MIN_POS) {
//            setPusherServoFallStartTime(System.currentTimeMillis());
//        }
//        if(getPusherServoFallStartTime() != -1) {
//            double passedTime = System.currentTimeMillis() - getPusherServoFallStartTime();
//            if(passedTime > PUSHER_SERVO_FALL_TIME) {
//                pusherServoPosition = PUSHER_SERVO_MAX_POS;
//                setPusherServoFallStartTime(-1);
//            } else {
//                pusherServoPosition = passedTime / PUSHER_SERVO_FALL_TIME * (PUSHER_SERVO_MAX_POS - PUSHER_SERVO_MIN_POS) + PUSHER_SERVO_MIN_POS;
//            }
//        }
//        getPusherServo().setPosition(pusherServoPosition);

        if(gamepad.y) {
            getPusherServo().setPosition(PUSHER_SERVO_MIN_POS);
        } else if(gamepad.a) {
            getPusherServo().setPosition(PUSHER_SERVO_MAX_POS);
        }
    }

    public void updatePullAdjusterServo(Gamepad gamepad) {
        if(gamepad.left_bumper && gamepad.right_bumper && gamepad.right_stick_y != 0) {
            double position = getPullAdjusterServo().getPosition();
            // Square right_stick_y for more precise control
            double newPosition = position + -(gamepad.right_stick_y * Math.abs(gamepad.right_stick_y)) * PULL_ADJUSTER_SERVO_SPEED;
            getPullAdjusterServo().setPosition(Range.clip(newPosition, PULL_ADJUSTER_SERVO_MIN_POS, PULL_ADJUSTER_SERVO_MAX_POS));
        }
    }

    public void updatePullUpMotor(Gamepad gamepad) {
        if(gamepad.left_bumper && gamepad.right_bumper && gamepad.left_stick_y != 0) {
            // Square left_stick_y for more precise control
            getPullUpMotor().setPower(gamepad.left_stick_y * Math.abs(gamepad.left_stick_y) * PULL_UP_MOTOR_MAX_POWER);
        } else {
            getPullUpMotor().setPower(0);
        }
    }

    public void updateConveyorMotor(Gamepad gamepad) {
        if(gamepad.left_trigger != 0) {
            getConveyorMotor().setPower(-Math.pow(gamepad.left_trigger, 2) * CONVEYOR_MOTOR_MAX_POWER);
        } else if(gamepad.right_trigger != 0) {
            getConveyorMotor().setPower(Math.pow(gamepad.right_trigger, 2) * CONVEYOR_MOTOR_MAX_POWER);
        } else {
            getConveyorMotor().setPower(0);
        }
    }

    public void stop() {
        getPullUpMotor().setPower(0);
        getConveyorMotor().setPower(0);
        getCatapultServo().setPosition(CATAPULT_SERVO_MIN_POS);
        getPusherServo().setPosition(PUSHER_SERVO_MIN_POS);
        getPullAdjusterServo().setPosition(PULL_ADJUSTER_SERVO_MIN_POS);
    }

    public DcMotor getPullUpMotor() {
        return pullUpMotor;
    }

    public void setPullUpMotor(DcMotor pullUpMotor) {
        this.pullUpMotor = pullUpMotor;
    }

    public Servo getCatapultServo() {
        return catapultServo;
    }

    public void setCatapultServo(Servo catapultServo) {
        this.catapultServo = catapultServo;
    }

    public Servo getPusherServo() {
        return pusherServo;
    }

    public void setPusherServo(Servo pusherServo) {
        this.pusherServo = pusherServo;
    }

//    public long getPusherServoFallStartTime() {
//        return pusherServoFallStartTime;
//    }
//
//    public void setPusherServoFallStartTime(long pusherServoFallStartTime) {
//        this.pusherServoFallStartTime = pusherServoFallStartTime;
//    }

    public Servo getPullAdjusterServo() {
        return pullAdjusterServo;
    }

    public void setPullAdjusterServo(Servo pullAdjusterServo) {
        this.pullAdjusterServo = pullAdjusterServo;
    }

    public DcMotor getConveyorMotor() {
        return conveyorMotor;
    }

    public void setConveyorMotor(DcMotor conveyorMotor) {
        this.conveyorMotor = conveyorMotor;
    }
}
