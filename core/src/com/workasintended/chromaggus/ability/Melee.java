package com.workasintended.chromaggus.ability;

import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/22/16.
 */
public class Melee extends OffensiveAbilityAdaptor {
    public Melee() {
        super(null, null);
        setCastingTime(0.5f);
        setCastRange(32f);
        setCooldown(1f);
        setCooldownProgress(1f);
    }

    @Override
    protected void useAbility() {
        takeEffect();
    }

    @Override
    protected void takeEffect() {
        getTarget().combat.takeDamage(getUser().combat.getStrength());
    }

    @Override
    public String toString() {
        return "Melee";
    }
}
