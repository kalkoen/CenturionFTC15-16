package com.koenvdberk.ftcrobot.hardware;

import com.koenvdberk.ftcrobot.ChangeListener;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

public abstract class HardwareListener<T, V> {

    private Map<T, ChangeListener> listeners;
    private Map<T, V> previousValues;

    public HardwareListener() {
        setListeners(new HashMap<T, ChangeListener>());
        setPreviousValues(new HashMap<T, V>());
    }

    public abstract V getValue(T t);

    public void update() {
        for(Map.Entry<T, ChangeListener> entry : getListeners().entrySet()) {
            update(entry.getKey(), entry.getValue());
        }
    }

    private void update(T t, ChangeListener changeListener) {
        V oldValue = getPreviousValues().get(t);
        V newValue = getValue(t);

        if(oldValue != null && newValue != null) {

            if (!newValue.equals(oldValue)) {
                changeListener.onChange();
            }

        }

        getPreviousValues().put(t, newValue);
    }

    public void registerListener(T t, ChangeListener changeListener) {
        getListeners().put(t, changeListener);
        getPreviousValues().put(t, getValue(t));
    }

    public void unregisterListener(T t) {
        getListeners().remove(t);
    }

    protected Map<T, ChangeListener> getListeners() {
        return listeners;
    }

    protected void setListeners(Map<T, ChangeListener> listeners) {
        this.listeners = listeners;
    }

    protected Map<T, V> getPreviousValues() {
        return previousValues;
    }

    protected void setPreviousValues(Map<T, V> previousValues) {
        this.previousValues = previousValues;
    }

    public static class DcMotorListener extends HardwareListener<DcMotor, Double> {
        @Override
        public Double getValue(DcMotor dcMotor) {
            return dcMotor.getPower();
        }
    }

    public static class ServoListener extends HardwareListener<Servo, Double> {
        @Override
        public Double getValue(Servo servo) {
            return servo.getPosition();
        }
    }

}
