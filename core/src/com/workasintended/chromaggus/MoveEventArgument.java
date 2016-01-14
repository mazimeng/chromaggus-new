package com.workasintended.chromaggus;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface MoveEventArgument {
	List<Unit> getUnits();
	Vector2 getTarget();
}
