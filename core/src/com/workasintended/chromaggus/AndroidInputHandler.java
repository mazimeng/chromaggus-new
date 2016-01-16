package com.workasintended.chromaggus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.workasintended.chromaggus.event.MoveUnitArgument;

/**
 * Created by mazimeng on 1/15/16.
 */
public class AndroidInputHandler extends ActorGestureListener {
    private WorldStage worldStage;
    private InputHandler inputHandler;

    public AndroidInputHandler(WorldStage worldStage) {
        this.worldStage = worldStage;
        inputHandler = new Selection(worldStage);
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        System.out.println(String.format("touchDown: %s, %s, %s, %s", x, y, pointer, button));
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println(String.format("%s, %s, %s, %s", x, y, pointer, button));
        this.inputHandler.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
//        System.out.println(String.format("tap: %s, %s, %s, %s", x, y, count, button));

    }

    @Override
    public boolean longPress(Actor actor, float x, float y) {
//        System.out.println(String.format("longPress: %s, %s, %s", x, y, actor.toString()));

        return super.longPress(actor, x, y);
    }

    @Override
    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
//        System.out.println(String.format("fling: %s, %s, %s", velocityX, velocityY, button));

    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        System.out.println(String.format("pan: %s, %s, %s, %s", x, y, deltaX, deltaY));

    }

    @Override
    public void zoom(InputEvent event, float initialDistance, float distance) {
        super.zoom(event, initialDistance, distance);
//        System.out.println(String.format("zoom: %s, %s", initialDistance, distance));

    }

    @Override
    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
//        System.out.println(String.format("pinch: %s, %s, %s, %s",
//                initialPointer1, initialPointer2,
//                pointer1, pointer2));
    }

    public WorldStage getWorldStage() {
        return worldStage;
    }

    public void setWorldStage(WorldStage worldStage) {
        this.worldStage = worldStage;
    }

    public class InputHandler extends ActorGestureListener {
        private WorldStage worldStage;

        public InputHandler(WorldStage worldStage) {
            this.worldStage = worldStage;
        }
    }

    public class Selection extends InputHandler {
        public Selection(WorldStage worldStage) {
            super(worldStage);
        }

        @Override
        public void tap(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            Actor actor = getWorldStage().hit(x, y, false);

            if (actor != null && actor instanceof Unit) {
                AndroidInputHandler.this.inputHandler = new Command(getWorldStage(), (Unit) actor);
            }
        }
    }

    public class Command extends InputHandler {
        private Unit unit;

        public Command(WorldStage worldStage, Unit unit) {
            super(worldStage);
            this.unit = unit;
        }

        @Override
        public void tap(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);

            Actor actor = getWorldStage().hit(x, y, false);
            if (actor == null) {
                Service.eventQueue().enqueue(new Event(EventName.MOVE_UNIT, new MoveUnitArgument(unit, new Vector2(x, y))));
            } else if(actor != this.unit){
                Selection selection = new Selection(getWorldStage());
                selection.touchUp(event, x, y, pointer, button);
                AndroidInputHandler.this.inputHandler = selection;

            }
        }
    }

    public class Camera extends InputHandler {
        public Camera(WorldStage worldStage) {
            super(worldStage);
        }
    }
}
