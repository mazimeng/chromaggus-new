package com.workasintended.chromaggus.event;

import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/19/16.
 */
public class UnitEvent extends Event {
    private Unit self;

    public UnitEvent(Unit self) {
        this.self = self;
    }

    public Unit getSelf() {
        return self;
    }

    public void setSelf(Unit self) {
        this.self = self;
    }
}
