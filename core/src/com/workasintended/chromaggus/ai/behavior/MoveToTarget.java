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
public class MoveToTarget extends LeafTask<Blackboard> {
    public MoveToTarget() {
//        setGuard(new TargetExists());
    }

    @Override
    public Status execute() {
        if(getStatus()!=Status.RUNNING) {
            Unit self = getObject().getSelf();
            Unit target = getObject().getTarget();
            self.movement.followUnit(target);
        }

        return Status.RUNNING;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }
}
