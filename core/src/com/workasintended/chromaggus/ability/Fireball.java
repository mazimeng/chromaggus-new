package com.workasintended.chromaggus.ability;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.ActorFactory;
import com.workasintended.chromaggus.Effect;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.action.EffectAction;
import com.workasintended.chromaggus.action.MoveToUnit;

/**
 * Created by mazimeng on 1/22/16.
 */
public class Fireball extends OffensiveAbilityAdaptor {
    public Fireball() {
        super(null, null);
        setCastingTime(2f);
        setCastRange(128f);
        setCooldown(0f);
        setCooldownProgress(0f);
    }

    @Override
    protected void useAbility() {
        Unit user = getUser();
        Unit target = getTarget();

        if(target.dead()) return;

        Effect fireball = ActorFactory.instance().fireball();
        fireball.setPosition(user.getX(Align.center), user.getY(Align.center), Align.center);

        MoveToUnit moveToUnit = new MoveToUnit(target, 256, 16);
        EffectAction effectAction = new EffectAction(this);
        RemoveActorAction removeActorAction = new RemoveActorAction();
        removeActorAction.setActor(fireball);

        SequenceAction sequenceAction = new SequenceAction(moveToUnit, effectAction, removeActorAction);
        fireball.addAction(sequenceAction);

        float angle = new Vector2(target.getX(Align.center), target.getY(Align.center))
                .sub(user.getX(Align.center), user.getY(Align.center))
                .angle();
        fireball.setRotation(angle);

        user.getWorld().addActor(fireball);
    }

    @Override
    protected void takeEffect() {
        getTarget().combat.takeDamage(getUser().combat.getIntelligence());
    }

    @Override
    public void update(float delta) {

    }
}
