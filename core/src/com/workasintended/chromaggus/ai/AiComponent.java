package com.workasintended.chromaggus.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.GameComponent;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;

public class AiComponent implements GameComponent {
	protected WorldStage stage;
	protected Unit unit;
	private AiState aiState;

	protected Unit closestUnit(Unit closeTo, float range) {
		Stage stage = unit.getStage();
		float min = Float.MAX_VALUE;
		Unit closest = null;
		for(Actor actor: stage.getActors()) {
			if(!(actor instanceof Unit)) continue;
			Unit u = (Unit)actor;
			
			if(u == this.unit || (u.getFaction()&this.unit.getFaction())>0) continue;
			if(u.dead()) continue;
			
			float d = Vector2.dst2(u.getX(), u.getY(), closeTo.getX(), closeTo.getY());
			if(d > range*range) continue;
			
			closest = d<=min? u:closest;			
			min = Math.min(d, min);
		}
		
		return closest;
	}
	@Override
	public void update(float delta) {
		if(this.aiState==null) return;
		this.aiState.update(delta);
	}

	public AiState getAiState() {
		return aiState;
	}

	public void setAiState(AiState aiState) {
		this.aiState = aiState;
		aiState.onEnter();
	}
}
