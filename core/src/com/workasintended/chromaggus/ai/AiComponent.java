package com.workasintended.chromaggus.ai;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.unitcomponent.UnitComponent;
import com.workasintended.chromaggus.WorldStage;

import java.util.LinkedList;

public class AiComponent extends UnitComponent {
	private WorldStage stage;
    private LinkedList<Action> previousActions = new LinkedList<>();

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

	protected boolean exit() {
        return false;
	}

    protected void saveActions() {

    }
}
