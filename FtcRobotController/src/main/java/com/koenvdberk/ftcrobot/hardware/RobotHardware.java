package com.koenvdberk.ftcrobot.hardware;

import com.koenvdberk.ftcrobot.drivercontrolled.Driver;
import com.koenvdberk.ftcrobot.drivercontrolled.MotionController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

// A class with predefined hardware that all op modes of this robot should use.
// This way it's easy to change names and channels of hardware without changing all op modes and there will be no confusion when initializing hardware.
public class RobotHardware {

    public static final String
            FRONT_MOTOR_CONTROLLER_NAME = "front_motor_controller",
            BACK_MOTOR_CONTROLLER_NAME = "back_motor_controller",
            FRONT_LEFT_MOTOR_NAME = "front_left_motor",
            FRONT_RIGHT_MOTOR_NAME = "front_right_motor",
            BACK_LEFT_MOTOR_NAME = "back_left_motor",
            BACK_RIGHT_MOTOR_NAME = "back_right_motor",

            SECONDARY_MOTOR_CONTROLLER_NAME = "secondary_motor_controller",
            PULL_UP_MOTOR_NAME = "pull_up_motor",
            CONVEYOR_MOTOR_NAME = "conveyor_motor",

            SERVO_CONTROLLER_NAME = "servo_controller",
            CATAPULT_SERVO_NAME = "catapult_servo",
            PUSHER_SERVO_NAME = "pusher_servo",
            PULL_ADJUSTER_SERVO_NAME = "pull_adjuster_servo";

    private HardwareMap hardwareMap;

    private DcMotorController frontMotorController, backMotorController, secondaryMotorController;
    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, pullUpMotor, conveyorMotor;

    private ServoController servoController;
    private Servo catapultServo, pusherServo, pullAdjusterServo;

    public RobotHardware(HardwareMap hardwareMap) {

        setHardwareMap(hardwareMap);

        // Initialize motors using specified device names and ids.

        setFrontMotorController(hardwareMap.dcMotorController.get(FRONT_MOTOR_CONTROLLER_NAME));
        setBackMotorController(hardwareMap.dcMotorController.get(BACK_MOTOR_CONTROLLER_NAME));

        setFrontLeftMotor(hardwareMap.dcMotor.get(FRONT_LEFT_MOTOR_NAME));
        setFrontRightMotor(hardwareMap.dcMotor.get(FRONT_RIGHT_MOTOR_NAME));
        setBackLeftMotor(hardwareMap.dcMotor.get(BACK_LEFT_MOTOR_NAME));
        setBackRightMotor(hardwareMap.dcMotor.get(BACK_RIGHT_MOTOR_NAME));


        // Left motor is turned the other way, so it has to be inverted. Why doesn't the right motor have to be inverted?
        // Setting the power of the right motor to 1 makes it move forward (unless we say the robot moves 'forward' when it goes the other way)
        getFrontLeftMotor().setDirection(DcMotor.Direction.REVERSE);
        getBackLeftMotor().setDirection(DcMotor.Direction.REVERSE);

        // Cache motor power
        setFrontLeftMotor(new CachedDcMotor(getFrontLeftMotor()));
        setFrontRightMotor(new CachedDcMotor(getFrontRightMotor()));
        setBackLeftMotor(new CachedDcMotor(getBackLeftMotor()));
        setBackRightMotor(new CachedDcMotor(getBackRightMotor()));

        setSecondaryMotorController(hardwareMap.dcMotorController.get(SECONDARY_MOTOR_CONTROLLER_NAME));

        setPullUpMotor(hardwareMap.dcMotor.get(PULL_UP_MOTOR_NAME));
        setConveyorMotor(hardwareMap.dcMotor.get(CONVEYOR_MOTOR_NAME));

        setServoController(hardwareMap.servoController.get(SERVO_CONTROLLER_NAME));

        setCatapultServo(new CachedServo(hardwareMap.servo.get(CATAPULT_SERVO_NAME)));
        setPusherServo(new CachedServo(hardwareMap.servo.get(PUSHER_SERVO_NAME)));
        setPullAdjusterServo(new CachedServo(hardwareMap.servo.get(PULL_ADJUSTER_SERVO_NAME)));

//        fixHardwareMap();
    }

//    public void fixHardwareMap() {
//        hardwareMap.dcMotor.put(FRONT_LEFT_MOTOR_NAME, getFrontLeftMotor());
//        hardwareMap.dcMotor.put(FRONT_RIGHT_MOTOR_NAME, getFrontRightMotor());
//        hardwareMap.dcMotor.put(BACK_LEFT_MOTOR_NAME, getBackLeftMotor());
//        hardwareMap.dcMotor.put(BACK_RIGHT_MOTOR_NAME, getBackRightMotor());
//        hardwareMap.servo.put(CATAPULT_SERVO_NAME, getCatapultServo());
//    }

    public void resetMotors() {
        getFrontLeftMotor().setPower(0);
        getFrontRightMotor().setPower(0);
        getBackLeftMotor().setPower(0);
        getBackRightMotor().setPower(0);
        getPullUpMotor().setPower(0);
    }

    public Driver getNewDriver() {
        return new Driver(getFrontLeftMotor(), getFrontRightMotor(), getBackLeftMotor(), getBackRightMotor());
    }

    public MotionController getNewMotionController() {
        return new MotionController(getPullUpMotor(), getConveyorMotor(), getCatapultServo(), getPusherServo(), getPullAdjusterServo());
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public DcMotorController getFrontMotorController() {
        return frontMotorController;
    }

    public void setFrontMotorController(DcMotorController frontMotorController) {
        this.frontMotorController = frontMotorController;
    }

    public DcMotorController getBackMotorController() {
        return backMotorController;
    }

    public void setBackMotorController(DcMotorController backMotorController) {
        this.backMotorController = backMotorController;
    }

    public DcMotor getFrontLeftMotor() {
        return frontLeftMotor;
    }

    public void setFrontLeftMotor(DcMotor frontLeftMotor) {
        this.frontLeftMotor = frontLeftMotor;
    }

    public DcMotor getFrontRightMotor() {
        return frontRightMotor;
    }

    public void setFrontRightMotor(DcMotor frontRightMotor) {
        this.frontRightMotor = frontRightMotor;
    }

    public DcMotor getBackLeftMotor() {
        return backLeftMotor;
    }

    public void setBackLeftMotor(DcMotor backLeftMotor) {
        this.backLeftMotor = backLeftMotor;
    }

    public DcMotor getBackRightMotor() {
        return backRightMotor;
    }

    public void setBackRightMotor(DcMotor backRightMotor) {
        this.backRightMotor = backRightMotor;
    }

    public DcMotorController getSecondaryMotorController() {
        return secondaryMotorController;
    }

    public void setSecondaryMotorController(DcMotorController secondaryMotorController) {
        this.secondaryMotorController = secondaryMotorController;
    }

    public DcMotor getPullUpMotor() {
        return pullUpMotor;
    }

    public void setPullUpMotor(DcMotor pullUpMotor) {
        this.pullUpMotor = pullUpMotor;
    }

    public ServoController getServoController() {
        return servoController;
    }

    public void setServoController(ServoController servoController) {
        this.servoController = servoController;
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

    public DcMotor getConveyorMotor() {
        return conveyorMotor;
    }

    public void setConveyorMotor(DcMotor conveyorMotor) {
        this.conveyorMotor = conveyorMotor;
    }

    public Servo getPullAdjusterServo() {
        return pullAdjusterServo;
    }

    public void setPullAdjusterServo(Servo pullAdjusterServo) {
        this.pullAdjusterServo = pullAdjusterServo;
    }
}
