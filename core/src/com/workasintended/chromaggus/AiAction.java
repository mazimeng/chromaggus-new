package com.workasintended.chromaggus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.ability.AbilityTarget;

public class AiAction {
//	private Unit unit;
//	private float responseRange = 128f;
//	
//	public AiAction(Unit unit) {
//		super();
//		this.unit = unit;
//	}
//	
//	public Unit getUnit() {
//		return unit;
//	}
//
//	public void setUnit(Unit unit) {
//		this.unit = unit;
//	}
//	
//	@Override
//	public boolean act(float delta) {
//		
//		Unit u = this.closest();
//		
//		if(u == null) return false;
//		
//		Ability ability = this.getUnit().getAbility(1);
//		AbilityTarget t = new AbilityTarget();
//		t.setLocation(new Vector2(u.getX(), u.getY()));
//		t.setUnit(u);
//		
//		if(ability.inRange(t)) {
//			ability.use(t);
//			return false;
//		}		
//		
//		Vector2 n = new Vector2(u.getX(), u.getY())
//			.sub(this.unit.getX(), this.unit.getY())
//			.nor()
//			.scl(delta * this.unit.getSpeed())
//			.add(this.unit.getX(), this.unit.getY());
//		this.unit.setPosition(n.x, n.y);
//
//		return false;
//	}
//	
//	protected Unit closest() {
//		Stage stage = unit.getStage();
//		float min = Float.MAX_VALUE;
//		Unit closest = null;
//		for(Actor actor: stage.getActors()) {
//			if(!(actor instanceof Unit)) continue;
//			Unit u = (Unit)actor;
//			
//			if(u == this.unit || (u.getFaction()&this.unit.getFaction())>0) continue;
//			if(u.dead()) continue;
//			
//			float d = Vector2.dst2(u.getX(), u.getY(), this.unit.getX(), this.unit.getY());
//			if(d > this.responseRange*this.responseRange) continue;
//			
//			closest = d<=min? u:closest;			
//			min = Math.min(d, min);
//		}
//		
//		return closest;
//	}

}
