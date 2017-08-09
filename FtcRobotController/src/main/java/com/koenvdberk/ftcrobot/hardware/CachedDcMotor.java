package com.koenvdberk.ftcrobot.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CachedDcMotor extends DcMotor {

    private double power = 0;

    public CachedDcMotor(DcMotor dcMotor) {
        super(dcMotor.getController(), dcMotor.getPortNumber(), dcMotor.getDirection());
    }

    @Override
    public double getPower() {
        return this.power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
        super.setPower(power);
    }
}
