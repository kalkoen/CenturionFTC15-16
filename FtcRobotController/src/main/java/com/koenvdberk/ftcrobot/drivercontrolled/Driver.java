package com.koenvdberk.ftcrobot.drivercontrolled;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

// This class is the default class for making the robot drive.
// This class enables you to control the left and right motor using one gamepad.
public class Driver {

    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    // Boring initialization
    public Driver(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor) {
        setFrontLeftMotor(frontLeftMotor);
        setFrontRightMotor(frontRightMotor);
        setBackLeftMotor(backLeftMotor);
        setBackRightMotor(backRightMotor);
    }

    public void drive(Gamepad gamepad) {

        // Motors are naturally off
        double rightPower = 0, leftPower = 0;

        // Cubing all input for very precise control. Squaring didn't feel like it was enough.

        // Right trigger represents moving forward. Power of motors increases as the trigger is pressed further.
        if(gamepad.right_trigger > 0) {
            rightPower = leftPower = Math.pow(gamepad.right_trigger, 3);
        }
        // Left trigger represents moving back. Power of motors negatively increases as the trigger is pressed further.
        else if(gamepad.left_trigger > 0) {
            rightPower = leftPower = -Math.pow(gamepad.left_trigger, 3);
        }

        // Pres
        // Using A will make robot turn. The robot will turn around the middle of the four motors.
        // Any trigger input will be ignored.
        if(gamepad.a) {
            // As the joystick is moved further to the left (negative x) the power of the left motors should decrease (negative), so we use joystick x.
            // As the joystick is moved further to the right (positive x) the power of the left motors should increase (positive). Using joystick x thus works fine.
            leftPower = Math.pow(gamepad.left_stick_x, 3);

            // As the joystick is moved further to the right (positive x) the power of the right motors should decrease (negative), so we invert joystick x.
            // As the joystick is moved further to the left (negative x) the power of the right motors should increase (positive). Inverting joystick x thus works fine.
            rightPower = -Math.pow(gamepad.left_stick_x, 3);
        }

        // Setting the power of the motors. The motors will be reset if nothing was pressed/moved.
        setLeftMotorPower(leftPower);
        setRightMotorPower(rightPower);
    }

    public void stop() {
        // Does this really do anything? Still doing it just in case.
        setLeftMotorPower(0);
        setRightMotorPower(0);
    }

    private void setLeftMotorPower(double power) {
        getFrontLeftMotor().setPower(power);
        getBackLeftMotor().setPower(power);
    }

    private void setRightMotorPower(double power) {
        getFrontRightMotor().setPower(power);
        getBackRightMotor().setPower(power);
    }

    // Y'all got any more of them getters and setters?
    // Of course.

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
}
