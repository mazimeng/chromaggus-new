package com.workasintended.chromaggus.ability;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.*;

/**
 * Created by mazimeng on 8/2/15.
 */
public class Melee implements Ability {
    private float castingTime = 1.5f;
    private float prep = 0f;

    private Unit unit;

    private int IDLE = 0;
    private int CASTING = 1;
    private int ABILITY_READY = 2;
    private int STOPPED = 3;

    private int state = IDLE;
    private int lastSecond = 0;
    private AbilityTarget abilityTarget;

    private AnimationRenderable animationRenderable;

    private float[][] direction = {
            {135f, 90f, 45f}, //00, 01, 02
            {180f, 90f, 0f}, //10, 11, 12
            {225f, 270f, 315f} //20, 21, 22
    };

    public Melee(Unit unit) {
        this.unit = unit;
    }

    @Override
    public void update(float delta) {
        if (state == CASTING && prep < castingTime) {
            prep += delta;
            if (lastSecond != (int) prep) {
                lastSecond = (int) prep;
            }

        } else if (state == CASTING && prep >= castingTime) {
            state = ABILITY_READY;

        } else if (state == ABILITY_READY) {
            melee();
            state = IDLE;
            prep = 0;
        }
    }

    @Override
    public void use(AbilityTarget target) {
        abilityTarget = target;

        if (state == IDLE) {
            state = CASTING;
        }
    }

    @Override
    public void stop() {
        state = STOPPED;
    }

    @Override
    public void reset() {
        state = IDLE;
        prep = 0;
    }

    private void melee() {
        if (abilityTarget.getUnit() == null) return;

        Unit target = abilityTarget.getUnit();
        if(target.hp <= 0) return;

        target.hp -= unit.strength;

        renderMelee(unit, abilityTarget);
    }

    private void renderMelee(Unit attacker, AbilityTarget abilityTarget) {
        if (animationRenderable != null) {
            Unit target = abilityTarget.getUnit();


            animationRenderable.setPosition(target.getX(), target.getY());
            animationRenderable.setRotation(determineDirection(attacker, target));
            animationRenderable.resetAnimation();


            Service.eventQueue().enqueue(new Event(EventName.RENDER_ANIMATION, animationRenderable));
        }
    }


    private float determineDirection(Unit attacker, Unit target) {
        //normalize to [0, 2]
        int x = (int) (target.getX() - attacker.getX());
        int y = (int) (target.getY() - attacker.getY());
        x = x == 0 ? 1 : x / Math.abs(x) + 1;
        y = y == 0 ? 1 : y / Math.abs(y) + 1;

        float degree = direction[x][y];
        return degree;
    }


    @Override
    public boolean usable(AbilityTarget target) {
        return true;
    }

    @Override
    public boolean inRange(AbilityTarget target) {
        return true;
    }

    public void setAnimationRenderable(AnimationRenderable animationRenderable) {
        this.animationRenderable = animationRenderable;
    }
}
