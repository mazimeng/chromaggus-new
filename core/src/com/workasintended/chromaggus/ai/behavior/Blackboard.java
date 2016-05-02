package com.workasintended.chromaggus.ai.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Predicate;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by mazimeng on 4/28/16.
 */
public class Blackboard {
    private Unit self;
    private Unit target;
    private LinkedList<Unit> enemies = new LinkedList<>();
    private Unit lastSeenEnemy;

    public Unit popEnemy() {
        if(enemies.isEmpty()) return null;
        return enemies.pop();
    }

    public void pushEnemy(Unit unit) {
        enemies.push(unit);
    }

    public Unit getSelf() {
        return self;
    }

    public void setSelf(Unit self) {
        this.self = self;
    }

    public WorldStage getWorldStage() {
        return self.getWorld();
    }

    public Unit findNearest(Iterable<Unit> units, Predicate<Unit> filter) {
        float minD = -1;
        Unit nearest = null;

        for (Unit unit : units) {
            if(!filter.evaluate(unit)) continue;

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

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public Unit getLastSeenEnemy() {
        return lastSeenEnemy;
    }

    public void setLastSeenEnemy(Unit lastSeenEnemy) {
        this.lastSeenEnemy = lastSeenEnemy;
    }
}
