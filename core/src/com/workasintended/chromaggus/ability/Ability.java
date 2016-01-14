package com.workasintended.chromaggus.ability;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface Ability {
	void update(float delta);
	void use(AbilityTarget target);
	void stop();
	void reset();
	boolean usable(AbilityTarget target);
	boolean inRange(AbilityTarget target);
	int ATTACK = 1;
	int MOVE = 2;
	int MELEE = 3;
}