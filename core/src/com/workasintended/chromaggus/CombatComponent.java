package com.workasintended.chromaggus;

import com.workasintended.chromaggus.ability.Ability;

public class CombatComponent implements GameComponent {
	private Ability[] abilities = new Ability[9];
	
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
	
	public void setAbility(int index, Ability ability) {
		this.abilities[index] = ability;
	}
	
	public Ability getAbility(int index) {
		return this.abilities[index];
	}
}
