package com.workasintended.chromaggus.event;

import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.EventName;
import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/19/16.
 */
public class DevelopCityEvent extends Event {
    private Unit city;
    public DevelopCityEvent(Unit city) {
        this.city = city;
    }

    public Unit getCity() {
        return city;
    }

    public void setCity(Unit city) {
        this.city = city;
    }
}
