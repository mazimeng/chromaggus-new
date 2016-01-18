package com.workasintended.chromaggus.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.GameComponent;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.UnitComponent;
import com.workasintended.chromaggus.WorldStage;

public class AiComponent extends UnitComponent {
	private WorldStage stage;

	public AiComponent(AiComponent previous) {
		this(previous.getSelf(), previous.getStage());
	}
	public AiComponent(Unit self, WorldStage stage) {
		super(self);
		this.stage = stage;
	}

	public WorldStage getStage() {
		return stage;
	}

	public void setStage(WorldStage stage) {
		this.stage = stage;
	}
}
