package com.koenvdberk.ftcrobot.autonomous;

import com.koenvdberk.ftcrobot.autonomous.actions.Action;

import java.util.ArrayList;
import java.util.List;

public class AutonomousScheduler extends Action {

    private List<Runnable> actions;

    public AutonomousScheduler(List<Runnable> actions) {
        setActions(actions);
    }

    public AutonomousScheduler() {
        this(new ArrayList<Runnable>());
    }

    @Override
    public void onRun() {
        for (int i = 0; i < getActions().size(); i++) {
            Runnable runnable = getActions().get(i);
            runnable.run();
            if (runnable instanceof Action) {
                Action action = (Action) runnable;

                if (action.hasEnded()) {
                    getActions().remove(i);
                    i--;
                } else {
                    break;
                }
            } else {
                getActions().remove(i);
                i--;
            }
        }
    }

    public void loop(final Action loop) {
        Runnable looper = new Runnable() {
            @Override
            public void run() {
                loop.run();
                if(!loop.hasEnded()) {
                    add(this);
                }
            }
        };
        // Might give a slight increase in performance.
        looper.run();
    }

    public boolean isEmpty() {
        return getActions().isEmpty();
    }

    public void add(Runnable runnable) {
        getActions().add(runnable);
    }

    public List<Runnable> getActions() {
        return actions;
    }

    public void setActions(List<Runnable> actions) {
        this.actions = actions;
    }
}
