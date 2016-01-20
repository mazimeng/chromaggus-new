package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;

public class CityComponent {
	private float radius = 64;
	private WorldStage stage;
	private Unit unit;
	private Float gold = 0f;
	private BitmapFont font;

	private float development = 0;

	public CityComponent(WorldStage stage, Unit unit, BitmapFont font) {
		this.unit = unit;
		//this.gold = new Attribute<Float>(0f, 100f);
		this.font = font;
		this.stage = stage;
	}
	
	public void update(float delta) {
		float gold = this.development * delta;
		this.stage.setPlayerGold(gold + this.stage.getPlayerGold());
	}
	
	public void draw(Batch batch) {
//		if(font!=null) font.draw(batch, String.format("%.1f/%.1f"
//				, this.gold.getValue(), this.gold.getMax())
//				, this.unit.getX(), this.unit.getY());
	}
	
	public void develop(float delta) {
		float speed = 1f/60f;
		Array<Actor> actors = this.stage.getActors();
		int sum = 0;
		for(Actor actor: actors) {
			if(!(actor instanceof Unit)) continue;
			if(actor == this.unit) continue;
			if(((Unit)actor).dead()) continue;
			
			Unit unit = (Unit)actor;
			float d2 = Vector2.dst2(unit.getX(), unit.getY(), this.unit.getX(), this.unit.getY());
			if(d2 > this.radius*this.radius) continue;
			
			sum++;
		}
		float val = speed * delta * sum;
		this.gold += val;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}	
	
	public WorldStage getStage() {
		return stage;
	}

	public void setStage(WorldStage stage) {
		this.stage = stage;
	}

	public Float getGold() {
		return gold;
	}

	public void setGold(Float gold) {
		this.gold = gold;
	}

	public float getDevelopment() {
		return development;
	}

	public void setDevelopment(float development) {
		this.development = development;
	}
}
