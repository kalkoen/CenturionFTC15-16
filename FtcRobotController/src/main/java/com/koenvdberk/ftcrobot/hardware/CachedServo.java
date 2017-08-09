package com.koenvdberk.ftcrobot.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class CachedServo extends Servo {

    private double position;

    public CachedServo(Servo servo) {
        super(servo.getController(), servo.getPortNumber(), servo.getDirection());
    }

    @Override
    public double getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(double position) {
        this.position = position;
        super.setPosition(position);
    }
}
