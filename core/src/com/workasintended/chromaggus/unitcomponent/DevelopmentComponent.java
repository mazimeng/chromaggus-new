package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.unitcomponent.UnitComponent;

/**
 * Created by mazimeng on 1/17/16.
 */
public class DevelopmentComponent extends UnitComponent {
    private Unit city;
    private float range = 64f;
    private float speed = 1.0f;

    public DevelopmentComponent(Unit self) {
        super(self);
    }

    @Override
    public void update(float delta) {
        if (city == null) return;
        if (city.city == null) return;

        float dst2 = Vector2.dst2(getSelf().getX(Align.center), getSelf().getY(Align.center),
                city.getX(Align.center), city.getY(Align.center));

        if(dst2 <= range*range) {
            float gold = city.city.getGold();
            gold = gold + speed * delta;
            city.city.setGold(gold);
        }
    }

    public Unit getCity() {
        return city;
    }

    public void setCity(Unit city) {
        this.city = city;
    }
}
