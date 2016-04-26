package com.workasintended.chromaggus.ai;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.unitcomponent.UnitComponent;
import com.workasintended.chromaggus.WorldStage;

public class AiComponent extends UnitComponent {
	private WorldStage stage;
	public final static int IDLE = 1;
	public final static int BUSY = 2;
	public final static int INTERRUPTABLE = 4;
	private int status = IDLE;

	public AiComponent(Unit self, WorldStage stage) {
		super(self);
		this.stage = stage;
	}

	@Override
	public void update(float delta) {

	}

	private void defend() {
		if((status & IDLE) == 0 && (status & INTERRUPTABLE) == 0) {
			return;
		}

		Unit city = findOurNearbyCity();
		float defenseRadius = 128;

		if(city == null) return;

		Iterable<Unit> units = stage.getUnits(city.getX(), city.getY(), defenseRadius);
		Unit nearest = null;
		float minD = -1;
		for (Unit unit : units) {
			if(unit.getFaction() == getSelf().getFaction()) continue;
			float d = Vector2.dst2(unit.getX(), unit.getY(), getSelf().getX(), getSelf().getY());
			if(minD == -1) {
				minD = d;
			}

			if(d <= minD) {
				minD = d;
				nearest = unit;
			}
		}

		if(nearest!=null) {
			status = BUSY;
			getSelf().combat.attack(nearest);
		}
	}

	private void develop() {
		if(status == IDLE) {
			if(getSelf().development==null) return;

			Unit city = findOurNearbyCity();
			if(city == null) return;

			status = BUSY & INTERRUPTABLE;
			getSelf().development.develop(city);
		}
	}

	private Unit findOurNearbyCity() {
		float minD = -1;
		Unit nearby = null;
		for (Unit unit : stage.getUnits()) {
			if(unit == getSelf()) continue;
			if(unit.getFaction() != getSelf().getFaction()) continue;
			if(unit.city==null) continue;

			float d = Vector2.dst2(unit.getX(), unit.getY(), getSelf().getX(), getSelf().getY());
			if(minD == -1) {
				minD = d;
			}

			if(d <= minD) {
				minD = d;
				nearby = unit;
			}
		}

		return nearby;
	}

	private Unit findNearest(Iterable<Unit> units, Unit target, boolean sameFaction) {
		float minD = -1;
		Unit nearest = null;

		for (Unit unit : units) {
			if(unit == getSelf()) continue;
			if((unit.getFaction() != target.getFaction()) && sameFaction) continue;
			else if(!sameFaction && (unit.getFaction() == target.getFaction())) continue;

			float d = Vector2.dst2(unit.getX(), unit.getY(), getSelf().getX(), getSelf().getY());
			if(minD == -1) {
				minD = d;
			}

			if(d <= minD) {
				minD = d;
				nearest = unit;
			}
		}

		return nearest;
	}
}
