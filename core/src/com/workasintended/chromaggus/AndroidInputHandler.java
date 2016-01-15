package com.workasintended.chromaggus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.workasintended.chromaggus.event.SelectionCompleted;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mazimeng on 1/15/16.
 */
public class AndroidInputHandler extends InputHandler{

    private Rectangle selectionBox;
    private Vector2 selectionBoxPivot;
    private boolean selecting = false;

    private boolean movingCamera = false;
    private Vector2 lastPointerPosition = new Vector2();

    private WorldStage worldStage;

    public AndroidInputHandler() {

    }

    public AndroidInputHandler(WorldStage worldStage) {
        this.worldStage = worldStage;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        System.out.println(String.format("touchDragged: (%s, %s), %s", x, y, pointer));
//        if (selecting) {
//            selectionBox.set(Math.min(x, selectionBoxPivot.x), Math.min(y, selectionBoxPivot.y),
//                    Math.abs(x - selectionBoxPivot.x), Math.abs(y - selectionBoxPivot.y));
//            Service.eventQueue().enqueue(new Event(EventName.SELECTING, selectionBox));
//        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            selectionBoxPivot = new Vector2(x, y);
            selectionBox = new Rectangle(selectionBoxPivot.x, selectionBoxPivot.y, 1, 1);
            Service.eventQueue().enqueue(new Event(EventName.SELECTION_STARTED, selectionBox));

            selecting = true;
        }

        if (button == Input.Buttons.MIDDLE) {
            movingCamera = true;
            lastPointerPosition.x = x;
            lastPointerPosition.y = y;
        }

        System.out.println(String.format("touchDown: (%s, %s), %s, %s", x, y, pointer, button));

        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            selecting = false;
            List<Unit> characters = selectCharacters();
//            getController().setUnits(characters);
            Service.eventQueue().enqueue(new SelectionCompleted(characters));

//            Grid grid = getStage().getGridMap().grid(x, y);
        }
        System.out.println(String.format("touchUp: (%s, %s), %s, %s", x, y, pointer, button));
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        System.out.println(String.format("mouseMoved: (%s, %s), %s, %s", x, y));

//        Vector2 scrolling = new Vector2();
//        float zone = 10;
//        float halfWidth = getStage().getViewport().getWorldWidth() * 0.5f;
//        float halfHeight = getStage().getViewport().getWorldHeight() * 0.5f;
//        Vector3 cameraPosition = getStage().getCamera().position;
//        float west = cameraPosition.x - halfWidth + zone;
//        float east = cameraPosition.x + halfWidth - zone;
//        float north = cameraPosition.y + halfHeight - zone;
//        float south = cameraPosition.y - halfHeight + zone;
//
//        if (x <= west) {
//            scrolling.x = -1;
//        }
//        if (x >= east) {
//            scrolling.x = 1;
//        }
//
//        if (y >= north) {
//            scrolling.y = 1;
//        }
//        if (y <= south) {
//            scrolling.y = -1;
//        }
//
//        Service.eventQueue().enqueue(new Event(EventName.MOVING_CAMERA, scrolling));
        return super.mouseMoved(event, x, y);
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
//		boolean execute = false;
//		if(keycode >= Keys.NUM_0 && keycode <= Keys.NUM_1) {
//			execute = true;
//		}
//
//		execute = execute && (this.getUnit() != null);
//
//		if(execute) {
//			ExecutionState state = new ExecutionState(this.getController());
//			this.getController().setState(state);
//			state.handle(event);
//		}

        return true;
    }

    private LinkedList<Unit> selectCharacters() {
        LinkedList<Unit> units = new LinkedList<>();

//        for (Actor actor : getStage().getActors()) {
//            if (!(actor instanceof Unit)) continue;
//
//            Rectangle boundingBox = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
//
//            if (selectionBox.overlaps(boundingBox)) {
//                units.add((Unit)actor);
//            }
//        }

        return units;
    }

    public WorldStage getWorldStage() {
        return worldStage;
    }

    public void setWorldStage(WorldStage worldStage) {
        this.worldStage = worldStage;
    }

    public static class Drag extends DragListener {

    }
    public static class Gesture extends ActorGestureListener {
        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);
            System.out.println(String.format("touchDown: %s, %s, %s, %s", x, y, pointer, button));
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            System.out.println(String.format("touchUp: %s, %s, %s, %s", x, y, pointer, button));

        }

        @Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
            System.out.println(String.format("tap: %s, %s, %s, %s", x, y, count, button));

        }

        @Override
        public boolean longPress(Actor actor, float x, float y) {
            System.out.println(String.format("longPress: %s, %s, %s", x, y, actor.toString()));

            return super.longPress(actor, x, y);
        }

        @Override
        public void fling(InputEvent event, float velocityX, float velocityY, int button) {
            super.fling(event, velocityX, velocityY, button);
            System.out.println(String.format("fling: %s, %s, %s", velocityX, velocityY, button));

        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            super.pan(event, x, y, deltaX, deltaY);
            System.out.println(String.format("pan: %s, %s, %s, %s", x, y, deltaX, deltaY));

        }

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            super.zoom(event, initialDistance, distance);
            System.out.println(String.format("zoom: %s, %s", initialDistance, distance));

        }

        @Override
        public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            super.pinch(event, initialPointer1, initialPointer2, pointer1, pointer2);
            System.out.println(String.format("pinch: %s, %s, %s, %s",
                    initialPointer1, initialPointer2,
                    pointer1, pointer2));

        }
    }
}
