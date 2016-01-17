package com.workasintended.chromaggus.event;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.EventName;
import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/17/16.
 */
public class AttackUnitEvent extends Event {
    private Unit attacker;
    private Unit target;


    public AttackUnitEvent(Unit attacker, Unit target) {
        super(EventName.ATTACK_UNIT, null);
        this.attacker = attacker;
        this.target = target;
    }

    public Unit getAttacker() {
        return attacker;
    }

    public void setAttacker(Unit attacker) {
        this.attacker = attacker;
    }

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }
}
