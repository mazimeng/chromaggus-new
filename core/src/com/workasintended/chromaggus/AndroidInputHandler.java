package com.workasintended.chromaggus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.workasintended.chromaggus.episode.Faction;
import com.workasintended.chromaggus.event.AttackUnitEvent;
import com.workasintended.chromaggus.event.CameraZoomEvent;
import com.workasintended.chromaggus.event.DebugRendererArgument;
import com.workasintended.chromaggus.event.MoveUnitArgument;
import com.workasintended.chromaggus.event.order.MoveToPositionEvent;

/**
 * Created by mazimeng on 1/15/16.
 */
public class AndroidInputHandler extends ActorGestureListener implements EventHandler {
    private WorldStage worldStage;
    private InputHandler inputHandler;
    private int faction = Faction.FACTION_A;

    public AndroidInputHandler(WorldStage worldStage) {
        this.worldStage = worldStage;
        inputHandler = new Selection(worldStage);
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        this.inputHandler.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        this.inputHandler.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int pointer, int button) {
        this.inputHandler.tap(event, x, y, pointer, button);

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
        this.inputHandler.pan(event, x, y, deltaX, deltaY);
    }

    @Override
    public void zoom(InputEvent event, float initialDistance, float distance) {
        super.zoom(event, initialDistance, distance);
        this.inputHandler.zoom(event, initialDistance, distance);
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

    @Override
    public void handle(Event event) {
        ((EventHandler)this.inputHandler).handle(event);
    }


    public class InputHandler extends ActorGestureListener {
        private WorldStage worldStage;

        public InputHandler(WorldStage worldStage) {
            this.worldStage = worldStage;
        }
    }

    public class Selection extends InputHandler implements EventHandler {
        Unit selected = null;

        public Selection(WorldStage worldStage) {
            super(worldStage);
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            Vector2 scrolling = new Vector2();
            if(selected!=null) {
                Service.eventQueue().enqueue(new Event(EventName.SET_DEBUG_RENDERER,
                        new DebugRendererArgument("direction_"+selected.hashCode(),
                                new DebugRenderer.LineRenderer(selected.getX(Align.center), selected.getY(Align.center),
                                        x, y))));

//                scrolling = new Vector2(deltaX*0.25f, deltaY*0.25f);
            }
            else {
                scrolling = new Vector2(-deltaX, -deltaY);
            }
            Service.eventQueue().enqueue(new Event(EventName.MOVING_CAMERA, scrolling));
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(selected!=null) {
                Actor actor = getWorldStage().hit(x, y, false);
                if(actor instanceof Unit && selected!=actor) {
                    Service.eventQueue().enqueue(new AttackUnitEvent(selected, (Unit)actor));
                }
                else if(actor==null) {
                    Service.eventQueue().enqueue(new MoveToPositionEvent(selected, new Vector2(x, y)));

                }
                selected = null;
            }
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Actor actor = getWorldStage().hit(x, y, false);
            System.out.println(String.format("touchDown: %s", actor));
            if((actor instanceof Unit) && ((Unit) actor).city!=null) {
                System.out.println(String.format("city: %s", ((Unit) actor).city.getGold()));
            }

            if(actor instanceof Unit) {
                Unit unit = (Unit)actor;
                if((unit.getFaction() & AndroidInputHandler.this.faction)>0) selected = (Unit)actor;
            }
        }

        @Override
        public void tap(InputEvent event, float x, float y, int pointer, int button) {
//            super.touchUp(event, x, y, pointer, button);
//            Actor actor = getWorldStage().hit(x, y, false);
//
//            if((actor instanceof Unit) && ((Unit) actor).city!=null) {
//                System.out.println(String.format("city: %s", ((Unit) actor).city.getGold()));
//            }
//
//            if((selected==null && (actor instanceof Unit))) {
//                selected = (Unit)actor;
//            }
//            else if(selected!=null && actor != null && selected!=actor) {
//                Service.eventQueue().enqueue(new Event(EventName.MOVE_UNIT, new MoveUnitArgument(selected, new Vector2(x, y))));
//            }
//            else if(selected!=null && actor==null){
//                Service.eventQueue().enqueue(new Event(EventName.MOVE_UNIT, new MoveUnitArgument(selected, new Vector2(x, y))));
//            }

//            System.out.println(String.format("selection tap: %s, %s, %s, %s, %s", actor, x, y, pointer, button));
//            if (actor != null) {
////                AndroidInputHandler.this.inputHandler = new Command(getWorldStage(), (Unit) actor);
//            }
//
//            if (actor != this.unit) {
//                Service.eventQueue().enqueue(new Event(EventName.MOVE_UNIT, new MoveUnitArgument(unit, new Vector2(x, y))));
//            } else {
//                Selection selection = new Selection(getWorldStage());
//                //selection.tap(event, x, y, pointer, button);
//                AndroidInputHandler.this.inputHandler = selection;
//
//            }
        }

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            this.selected = null;
            int dir = distance>initialDistance?1:-1;
            Service.eventQueue().enqueue(new Event(EventName.CAMERA_ZOOM,
                    new CameraZoomEvent(dir)));

        }

        @Override
        public void handle(Event event) {
            if(event.getName()==EventName.CANCEL_SELECTION) {
                this.selected = null;
                System.out.println("selection cancelled");
            }
        }
    }
}
