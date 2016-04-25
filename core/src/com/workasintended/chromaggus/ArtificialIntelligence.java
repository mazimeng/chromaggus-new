package com.workasintended.chromaggus;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mazimeng on 4/14/16.
 */
public class ArtificialIntelligence {
    private Faction faction;
    private WorldStage worldStage;
    private List<Unit> notBusyUnits;
    private List<Unit> busyUnits;

    public void act(float delta) {
    }

    private void defendCity() {
        Unit city = null;
        List<Unit> nearbyUnits = null;
        List<Unit> nearbyEnemies = null;

        
    }

    private void sendUnitsToDefendCity() {

    }

    private boolean needDefense() {
        return false;
    }

    private boolean needAttack() {
        return false;
    }

    private boolean needDevelop() {
        return false;
    }

    private List<Unit> enemiesOnSight() {
        return new LinkedList<>();
    }

    private List<Unit> getNotBusyUnits() {
        return new LinkedList<>();
    }


    public static class Task {
        protected void onComplete() {

        }

        protected boolean done() {
            return true;
        }
    }

    public static class DefendCityTask extends Task {
        private Unit city;
        private List<Unit> team;

        private int alive() {
            return 0;
        }


    }
}
