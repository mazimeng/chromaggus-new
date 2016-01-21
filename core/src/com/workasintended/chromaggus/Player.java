package com.workasintended.chromaggus;

import com.workasintended.chromaggus.event.BuyItemEvent;
import com.workasintended.chromaggus.event.GainGoldEvent;
import com.workasintended.chromaggus.event.UnitSelectionEvent;

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
