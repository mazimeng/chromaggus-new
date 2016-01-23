package com.workasintended.chromaggus.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.ability.Ability;

/**
 * Created by mazimeng on 1/22/16.
 */
public class UseAbility extends Action {
    private Ability ability;
    private Unit user;
    private MoveToUnit moveToUnit;

    public UseAbility(Ability ability, Unit user, Unit target) {
        this.ability = ability;
        this.user = user;
        this.moveToUnit = new MoveToUnit(target);
        this.moveToUnit.setActor(user);
    }
    @Override
    public boolean act(float delta) {
        if(ability.inRange()) {
            if(ability.cast(delta)) {
                ability.use();
                System.out.println(ability.toString() + " used");
                return true;
            }
        }
        else {
            ability.reset();
            moveToUnit.act(delta);
        }
        return false;
    }
}
