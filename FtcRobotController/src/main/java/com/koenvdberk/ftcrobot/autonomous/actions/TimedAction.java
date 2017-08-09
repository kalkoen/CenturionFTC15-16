package com.koenvdberk.ftcrobot.autonomous.actions;

public class TimedAction extends Action {

    private long startTime;
    private int time;

    public TimedAction(int time) {
        setStartTime(-1);
        setTime(time);
    }

    public void start() {
        setStartTime(System.currentTimeMillis());
        super.start();
    }

    @Override
    public void run() {
        // Setting time to -1 will make this TimedAction run forever, unless stopped by another class.
        // Mainly implemented for ConditionAction
        if(getTimePassed() >= getTime() && getTime() != -1 && getStartTime() != -1) {
            stop();
            return;
        }

        super.run();
    }

    @Override
    public void onRun() {}

    public long getTimePassed() {
        return System.currentTimeMillis() - getStartTime();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
