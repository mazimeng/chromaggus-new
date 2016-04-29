package com.workasintended.chromaggus.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Predicate;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;

/**
 * Created by mazimeng on 4/28/16.
 */
public class ScanNearbyEnemy extends LeafTask<Blackboard> {
    private float radius = 128;
    @Override
    public Status execute() {
        final Blackboard b = getObject();
        WorldStage worldStage = b.getWorldStage();
        Unit enemy = b.findNearest(worldStage.getUnits(), new Predicate<Unit>() {
            @Override
            public boolean evaluate(Unit u) {
                return u.combat!=null && u.getFaction()!=b.getSelf().getFaction();
            }
        });

        if(enemy!=null) {
            Unit self = getObject().getSelf();
            float d2 = Vector2.dst2(enemy.getX(), enemy.getY(), self.getX(), self.getY());
            if(d2 > radius*radius) enemy = null;
        }

        b.setTarget(enemy);

        return enemy!=null?
                Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }
}
