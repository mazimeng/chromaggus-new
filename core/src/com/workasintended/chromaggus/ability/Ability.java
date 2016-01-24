package com.workasintended.chromaggus.ability;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.workasintended.chromaggus.Unit;

public interface Ability {
	void update(float delta);
	float getCastingTime();
	void use();
	boolean inRange();
	boolean cast(float delta);
	void reset();
	void setTarget(Unit target);
	void setUser(Unit unit);
	float getCastRange();
	void effect();
	float getCooldownProgress();
	float getCastingProgress();
	void setCastingProgress(float castingProgress);
	boolean isCasting();

}