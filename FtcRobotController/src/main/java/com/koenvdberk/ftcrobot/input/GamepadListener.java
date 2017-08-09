package com.koenvdberk.ftcrobot.input;

import com.koenvdberk.ftcrobot.ChangeListener;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamepadListener {

    private Gamepad gamepad, previousValues;
    private List<InputListener> inputListeners;

    public GamepadListener() {
        setInputListeners(new ArrayList<InputListener>());
    }

    public GamepadListener(Gamepad gamepad) {
        this();
        setGamepad(gamepad);
        setPreviousValues(new Gamepad());
    }

    public GamepadListener(Gamepad gamepad, InputListener... inputListeners) {
        this(gamepad);
        Collections.addAll(getInputListeners(), inputListeners);

    }

    public void update() {
        if(getGamepad() == null || getPreviousValues() == null) {
            return;
        }

        for(InputListener inputListener : getInputListeners()) {
            update(inputListener);
        }

        updatePreviousValues();
    }

    protected void update(InputListener inputListener) {
        Object value = inputListener.getValue(getGamepad());
        Object previousValue = inputListener.getValue(getPreviousValues());

        if(!value.equals(previousValue)) {
            inputListener.onChange();
        }
    }

    public void registerListener(InputListener inputListener) {
        getInputListeners().add(inputListener);
    }

    public void unregisterListener(InputListener inputListener) {
        getInputListeners().remove(inputListener);
    }

    protected void updatePreviousValues() {
        try {
            getPreviousValues().copy(getGamepad());
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }
    }

    public Gamepad getGamepad() {
        return gamepad;
    }

    public void setGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    protected Gamepad getPreviousValues() {
        return previousValues;
    }

    protected void setPreviousValues(Gamepad previousValues) {
        this.previousValues = previousValues;
    }

    protected List<InputListener> getInputListeners() {
        return inputListeners;
    }

    protected void setInputListeners(List<InputListener> inputListeners) {
        this.inputListeners = inputListeners;
    }

    public interface InputListener extends ChangeListener {
        // We're simply using Object, as we will only use this value to compare the current input and previous input
        Object getValue(Gamepad gamepad);
    }

}
