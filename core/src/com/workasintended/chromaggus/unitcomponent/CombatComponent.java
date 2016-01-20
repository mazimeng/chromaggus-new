package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.action.MoveToUnit;

public class CombatComponent extends UnitComponent {
	private Ability[] abilities = new Ability[9];

	public CombatComponent(Unit self) {
		super(self);
	}

	@Override
	public void update(float delta) {
		this.updateAbilities(delta);
	}

	protected void updateAbilities(float delta) {
		for(Ability ability: this.abilities) {
			if(ability == null) continue;
			ability.update(delta);
		}
	}

	public void attack(Unit target) {
		if(target==getSelf()) return;

		SequenceAction sequenceAction = new SequenceAction(new MoveToUnit(target), new com.workasintended.chromaggus.action.Attack(target));
		RepeatAction repeatAction = new RepeatAction();
		repeatAction.setAction(sequenceAction);
		repeatAction.setCount(RepeatAction.FOREVER);
		Action action = repeatAction;
		getSelf().clearActions();
		getSelf().addAction(action);
	}
	
	public void setAbility(int index, Ability ability) {
		this.abilities[index] = ability;
	}
	
	public Ability getAbility(int index) {
		return this.abilities[index];
	}
}
