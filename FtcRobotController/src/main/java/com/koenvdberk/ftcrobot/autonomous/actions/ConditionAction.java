package com.koenvdberk.ftcrobot.autonomous.actions;

public abstract class ConditionAction extends TimedAction {

    public ConditionAction() {
        this(-1);
    }

    public ConditionAction(int timeLimit) {
        super(timeLimit);
    }


    // Returns the condition to check for several times every second.
    public abstract boolean checkCondition();

    // Called when checkCondition() returns true.
    // *Returning true will keep the TimedAction running!*
    public abstract boolean onSuccess();

    // Called every time checkCondition() returns false.
    public void onFail() {}

    @Override
    public void run() {
        super.run();

        if(checkCondition()) {
            if(!onSuccess()) {
                stop();
            }
        } else {
            onFail();
        }
    }
}
