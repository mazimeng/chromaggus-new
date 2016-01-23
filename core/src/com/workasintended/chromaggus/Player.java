package com.workasintended.chromaggus;

import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.action.Attack;
import com.workasintended.chromaggus.action.UseAbility;
import com.workasintended.chromaggus.event.BuyItemEvent;
import com.workasintended.chromaggus.event.GainGoldEvent;
import com.workasintended.chromaggus.event.UnitSelectionEvent;
import com.workasintended.chromaggus.event.UseAbilityEvent;

/**
 * Created by mazimeng on 1/21/16.
 */
public class Player implements EventHandler {
    private Faction faction;
    private Unit selected = null;
    private float gold = 0;

    public Player() {
    }

    @Override
    public void handle(Event event) {
        if(event.is(EventName.UNIT_SELECTED)) {
            UnitSelectionEvent unitSelectedEvent = event.cast(UnitSelectionEvent.class);
            selected = unitSelectedEvent.getUnit();

            if(selected.combat!=null) System.out.println(selected.combat);
        }
        if(event.is(EventName.UNIT_DESELECTED)) {
            selected = null;
        }
        if(event.is(EventName.GAIN_GOLD)) {
            GainGoldEvent gainGoldEvent = event.cast(GainGoldEvent.class);
            this.gold += gainGoldEvent.getGold();
            System.out.println(String.format("player gain gold: %s, %s",
                    gainGoldEvent.getGold(),
                    this.gold));
        }

        handleBuyItem(event);
        handleUseAbility(event);
    }

    private void handleUseAbility(Event event) {
        if(!event.is(EventName.USE_ABILITY)) return;
        if(selected==null) return;


        UseAbilityEvent useAbilityEvent = event.cast(UseAbilityEvent.class);
        Ability ability = useAbilityEvent.getAbility();
        ability.setUser(selected);
        ability.setTarget(useAbilityEvent.getTarget());

        UseAbility useAbility = new UseAbility(ability, selected, useAbilityEvent.getTarget());
        Attack attack = new Attack(useAbilityEvent.getTarget());
        RepeatAction repeatAttack = new RepeatAction();
        repeatAttack.setAction(attack);
        repeatAttack.setCount(RepeatAction.FOREVER);
        SequenceAction sequenceAction = new SequenceAction(useAbility, repeatAttack);
        selected.clearActions();
        selected.addAction(sequenceAction);

        System.out.println("casting " + ability.toString());

    }

    private void handleBuyItem(Event event) {
        if(!event.is(EventName.BUY_ITEM)) return;
        if(selected==null) return;
        if(selected.combat == null) return;

        float cost = 1000;
        if(gold<cost) return;

        gold -= cost;
        selected.combat.gainExperience(300);
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
