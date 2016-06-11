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
public class TargetEnemy extends LeafTask<Blackboard> {
    private float radius = 128;
    @Override
    public Status execute() {
        final Blackboard b = getObject();

        Unit enemy = b.getLastSeenEnemy();
        b.setTarget(enemy);
        if(enemy != null) {

        }
        return Status.SUCCEEDED;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }

    @Override
    public String toString() {
        return "TargetEnemy";
    }
}
