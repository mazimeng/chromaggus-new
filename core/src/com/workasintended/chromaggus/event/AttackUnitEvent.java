package com.workasintended.chromaggus.event;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.EventName;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.event.order.UnitEvent;

/**
 * Created by mazimeng on 1/17/16.
 */
public class AttackUnitEvent extends UnitEvent {
    private Unit target;


    public AttackUnitEvent(Unit attacker, Unit target) {
        super(attacker, EventName.ATTACK_UNIT);
        this.target = target;
    }

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }
}
