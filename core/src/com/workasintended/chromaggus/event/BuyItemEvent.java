package com.workasintended.chromaggus.event;

import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.EventName;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.event.order.UnitEvent;

/**
 * Created by mazimeng on 1/21/16.
 */
public class BuyItemEvent extends Event{
    public BuyItemEvent() {
        super(EventName.BUY_ITEM);
    }
}
