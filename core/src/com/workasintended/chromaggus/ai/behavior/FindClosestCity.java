package com.workasintended.chromaggus.ai.behavior;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Predicate;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.WorldStage;

/**
 * Created by mazimeng on 5/2/16.
 */
public class FindClosestCity extends LeafTask<Blackboard> {
    @Override
    public Task.Status execute() {
        final Blackboard b = getObject();
        WorldStage worldStage = b.getWorldStage();
        Unit city = b.getTarget();

        if(city == null) {
            city = b.findNearest(worldStage.getUnits(), new Predicate<Unit>() {
                @Override
                public boolean evaluate(Unit u) {
                    return u.city!=null && u.getFaction()==b.getSelf().getFaction();
                }
            });
        }

        b.setTarget(city);

        return city!=null?
                Task.Status.SUCCEEDED : Task.Status.FAILED;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }
}