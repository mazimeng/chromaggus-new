package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.workasintended.chromaggus.Service;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;
import com.workasintended.chromaggus.event.GainGoldEvent;

public class CityComponent extends UnitComponent {
    private float radius = 64;
    private BitmapFont font;

    private float development = 0;
    private float incomeInterval = 3;
    private float progress = 0;

    private float restoreCooldown = 3;
    private float restore = 0;

    public CityComponent(Unit self) {
        super(self);
    }

//	public CityComponent(WorldStage stage, Unit unit, BitmapFont font) {
//		this.unit = unit;
//		//this.gold = new Attribute<Float>(0f, 100f);
//		this.font = font;
//		this.stage = stage;
//	}

    public void update(float delta) {
        tax(delta);
        restore(delta);
    }

    public void draw(Batch batch) {
//		if(font!=null) font.draw(batch, String.format("%.1f/%.1f"
//				, this.gold.getValue(), this.gold.getMax())
//				, this.unit.getX(), this.unit.getY());
    }

    private void tax(float delta) {
        progress += delta;
        if (progress > incomeInterval) {
            progress = 0;
            float gold = development;
            Service.eventQueue().enqueue(new GainGoldEvent(getSelf(), gold));
        }
    }

    private void restore(float delta) {
        restore += delta;
        if (restore < restoreCooldown) return;
        restore = 0;

        for (Actor actor : getSelf().getWorld().getActors()) {
            if (actor == getSelf()) continue;
            if (!(actor instanceof Unit)) continue;

            Unit unit = (Unit) actor;

            if (!unit.getFaction().isFriend(getSelf().getFaction())) continue;
            if (unit.city != null) continue;
            if (unit.combat == null) continue;
            if (unit.combat.getHp() == unit.combat.getMaxHp()) continue;

            if (Vector2.dst2(unit.getX(Align.center), unit.getY(Align.center),
                    getSelf().getX(Align.center), getSelf().getY(Align.center)) > 64 * 64) continue;

            unit.combat.setHp(unit.combat.getHp() + 3);

        }
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDevelopment() {
        return development;
    }

    public void setDevelopment(float development) {
        this.development = development;
    }


}
