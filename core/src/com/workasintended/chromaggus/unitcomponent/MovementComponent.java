package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.action.MoveToPosition;

/**
 * Created by mazimeng on 7/18/15.
 */
public class MovementComponent extends UnitComponent{
    public MovementComponent(Unit self) {
        super(self);
    }

    public void moveToPosition(Vector2 position) {
        Action action = new MoveToPosition(position, getSelf().getSpeed());
        getSelf().clearActions();
        getSelf().addAction(action);
    }
}
