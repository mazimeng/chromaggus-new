package com.workasintended.chromaggus.ability;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/22/16.
 */
public class Fireball implements Ability {
    private float castingTime = 0;
    private Unit user;
    private Unit target;
    private float castRange;
    private float progress = 0;

    public Fireball() {
        this(2f, null, null, 96);
    }
    public Fireball(Unit user, Unit target) {
        this(2f, user, target, 96);
    }


    public Fireball(float castingTime, Unit user, Unit target, float castRange) {
        this.castingTime = castingTime;
        this.user = user;
        this.target = target;
        this.castRange = castRange;
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public float getCastingTime() {
        return 0;
    }

    @Override
    public void use() {
        if(target.combat !=null && inRange()) {
            target.combat.setHp(target.combat.getHp()-3);
        }
    }

    @Override
    public boolean inRange() {
        float dst2 = Vector2.dst2(user.getX(Align.center), user.getY(Align.center),
                target.getX(Align.center), target.getY(Align.center));
        return dst2 <= castRange*castRange;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public boolean cast(float delta) {
        progress = MathUtils.clamp(progress+delta, 0, castingTime);
        return progress==castingTime;
    }

    @Override
    public void reset() {
        this.progress = 0;
    }

    @Override
    public void setTarget(Unit target) {
        this.target = target;
    }

    @Override
    public void setUser(Unit unit) {
        this.user = unit;
    }

    public Unit getUser() {
        return user;
    }

    public Unit getTarget() {
        return target;
    }

    public float getCastRange() {
        return castRange;
    }

    @Override
    public String toString() {
        return "Fireball";
    }
}
