package com.workasintended.chromaggus.action;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.*;
import com.workasintended.chromaggus.event.DebugRendererArgument;

/**
 * Created by mazimeng on 1/16/16.
 */
public class MoveToPosition extends Action {
    private Vector2 direction;
    private int alignment = Align.center;
    private Vector2 targetPosition;
    private boolean began;


    public MoveToPosition(Vector2 targetPosition) {
        this.targetPosition = new Vector2(targetPosition);
    }

    @Override
    public boolean act(float delta) {
        if(!began) {
            begin();
        }

        if(completed(delta)) {
            return true;
        }

        move(delta);
        return false;
    }

    protected boolean completed(float delta) {
        Unit unit = getUnit();
        Vector2 position = new Vector2(unit.getX(alignment), unit.getY(alignment));

        float nextDistance = delta * unit.getSpeed();

        float nextDistance2 = nextDistance*nextDistance;
        float distanceToTarget2 = position.dst2(targetPosition.x, targetPosition.y);

        if(nextDistance2 >= distanceToTarget2) {
            unit.setPosition(targetPosition.x, targetPosition.y, alignment);
            return true;
        }

        return false;
    }

    protected void move(float delta) {
        Unit unit = getUnit();
        Vector2 position = nextPosition(delta);
        unit.setPosition(position.x, position.y, alignment);
    }

    protected Vector2 nextPosition(float delta) {
        Unit unit = getUnit();
        float nextDistance = delta * unit.getSpeed();
        Vector2 position = new Vector2(unit.getX(alignment), unit.getY(alignment));
        Vector2 velocity = new Vector2(direction).scl(nextDistance);
        position = position.add(velocity);
        return position;
    }

    protected void begin() {
        Unit unit = getUnit();
        Vector2 position = new Vector2(unit.getX(alignment), unit.getY(alignment));
        this.direction = new Vector2(targetPosition).sub(position).nor();
        this.began = true;

        Service.eventQueue().enqueue(new Event(EventName.SET_DEBUG_RENDERER,
                new DebugRendererArgument("direction_"+unit.hashCode(), new DebugRenderer.LineRenderer(position.x, position.y, targetPosition.x, targetPosition.y))));
    }

    protected Unit getUnit() {
        return (Unit)this.getActor();
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }
}
