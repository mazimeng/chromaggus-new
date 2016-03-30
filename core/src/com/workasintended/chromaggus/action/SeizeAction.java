package com.workasintended.chromaggus.action;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.actions.EventAction;

/**
 * Created by mazimeng on 3/30/16.
 */
public class SeizeAction extends EventAction {
    private float progress = 0;

    public SeizeAction() {
        super(Event.class);

        System.out.println("seize started");
    }

    @Override
    public boolean handle(Event event) {
        boolean handled = (event instanceof TakeDamageActorEvent);

        if(handled) {
            System.out.println("seize interrupted");

            //set target to null to remove the listener
            this.setTarget(null);
        }

        return handled;
    }

    @Override
    public boolean act(float delta) {
        progress += delta;
        boolean done = super.act(delta) || progress >= 5;

        if(done) {
            System.out.println("seize complete");
            this.setTarget(null);
        }

        return done;
    }
}
