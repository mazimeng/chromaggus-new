package com.workasintended.chromaggus.ai;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.*;
import com.workasintended.chromaggus.event.MoveUnitArgument;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;

import java.util.LinkedList;
import java.util.List;

public class StateDefense extends AiState {
	private Unit target;
	private Vector2 start;

	public StateDefense(Unit unit, Unit target) {
		super(unit);
		this.target = target;
	}

	@Override
	public void update(float delta) {
		float d = Math.abs(getUnit().getX()-this.start.x) + Math.abs(getUnit().getY() - this.start.y);
		float range = 32 * 6;
		GridMap gridMap = getUnit().getWorld().getGridMap();
		if(d >= range) {
			System.out.println("set returnTo");
			getUnit().ai.setAiState(new StateReturn(getUnit(), this.start));
		}
	}

	@Override
	public void onEnter() {
		this.start = new Vector2(getUnit().getX(), getUnit().getY());
//		Service.eventQueue().enqueue(new Event(EventName.MOVE, new MoveEventArgument() {
//			@Override
//			public List<Unit> getUnits() {
//				LinkedList<Unit> units = new LinkedList<Unit>();
//				units.add(getUnit());
//				return units;
//			}
//
//			@Override
//			public Vector2 getTarget() {
//				return new Vector2(target.getX(), target.getY());
//			}
//		}));

		Service.eventQueue().enqueue(new Event(EventName.MOVE_UNIT, new MoveUnitArgument() {
			@Override
			public Unit getUnit() {
				return StateDefense.this.getUnit();
			}

			@Override
			public Vector2 getTarget() {
				return new Vector2(target.getX(), target.getY());
			}
		}));
	}


	public Unit getTarget() {
		return target;
	}

	public void setTarget(Unit target) {
		this.target = target;
	}


}
