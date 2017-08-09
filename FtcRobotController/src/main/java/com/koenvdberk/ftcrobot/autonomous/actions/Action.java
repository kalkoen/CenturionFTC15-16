package com.koenvdberk.ftcrobot.autonomous.actions;

public abstract class Action implements Runnable {

    private boolean ended, running;

    public Action() {
        setEnded(false);
        setRunning(false);
    }

    // Should be used carefully
    public void start() {
        setRunning(true);
        onStart();
    }

    @Override
    public void run() {
        if(!isRunning()) {
            start();
        }

        // Fail safe
        if(!hasEnded()) {
            onRun();
        }
    }

    public void stop() {
        setEnded(true);
        onStop();
    }

    public abstract void onRun();
    public void onStart() {}
    public void onStop() {}

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public boolean hasEnded() {
        return ended;
    }

    private void setEnded(boolean ended) {
        this.ended = ended;
    }
}
