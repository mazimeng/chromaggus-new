package com.workasintended.chromaggus.event;

import com.workasintended.chromaggus.EventName;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.event.order.UnitEvent;

/**
 * Created by mazimeng on 1/20/16.
 */
public class UnitSelectedEvent extends UnitEvent {
    public UnitSelectedEvent(Unit self) {
        super(self, EventName.UNIT_SELECTED);
    }
}
