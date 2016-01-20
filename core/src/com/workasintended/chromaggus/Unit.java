package com.workasintended.chromaggus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.ability.Melee;
import com.workasintended.chromaggus.action.MoveToPosition;
import com.workasintended.chromaggus.ai.AiComponent;
import com.workasintended.chromaggus.event.DevelopCityEvent;
import com.workasintended.chromaggus.event.order.MoveToPositionEvent;
import com.workasintended.chromaggus.order.Idle;
import com.workasintended.chromaggus.order.Order;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.unitcomponent.*;

import java.util.LinkedList;

public class Unit extends Group {
    public int hp;
    public int strength;
    public float radius = 32;
    public float speed = 32;
    private int faction = 0;
    private int experience = 0;
    private int experienceToLevelUp = 100;
    private int experienceGrowth = 20;
    private float attributeGrowth = 1.2f;


    public Sprite highlight;

    private Sprite sprite;
    private boolean highlighted = false;

    private BitmapFont font;

    public AiComponent ai;
    public CombatComponent combat;
    public CityComponent city;
    public MovementComponent movement;
    public RendererComponent renderer;
    public DevelopmentComponent development;

    private Order order = new Idle();

    public Ability[] abilities;

    public Grid currentGrid;

    private LinkedList<UnitTask> taskQueue = new LinkedList<>();
    private UnitTask executingTask = null;

    public Unit() {
        abilities = new Ability[9];
        abilities[Ability.MELEE] = new Melee(this);


        this.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer,
                              Actor fromActor) {
                Unit.this.highlighted = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor toActor) {
                Unit.this.highlighted = false;
            }

        });

    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.setSize(sprite.getWidth(), sprite.getHeight());
        //this.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    @Override
    public void act(float delta) {
        if (this.dead()) {
            this.remove();
            Service.eventQueue().enqueue(new Event(EventName.UNIT_DIED, this));
        }
        super.act(delta);

        if (this.city != null) this.city.update(delta);
        if (this.ai != null) this.ai.update(delta);
        if (this.combat != null) this.combat.update(delta);

        if (this.order != null) this.order.update(delta);
        if (this.development != null) this.development.update(delta);

        for (Ability ability : abilities) {
            if (ability == null) continue;
            ability.update(delta);
        }
    }


    public boolean isHighlighted() {
        return highlighted;
    }


    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (this.renderer != null) {
            renderer.draw(batch, parentAlpha);
        }

        if (this.sprite != null) batch.draw(sprite
                , this.getX(), this.getY()
                , this.getOriginX(), this.getOriginY()
                , this.getWidth(), this.getHeight()
                , this.getScaleX(), this.getScaleY()
                , this.getRotation());

        if (font != null) font.draw(batch, Integer.toString(this.hp), this.getX(), this.getY());
        if (this.city != null) this.city.draw(batch);

        if (this.highlight != null) batch.draw(highlight
                , this.getX(), this.getY()
                , this.getOriginX(), this.getOriginY()
                , this.getWidth(), this.getHeight()
                , this.getScaleX(), this.getScaleY()
                , 0);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean dead() {
        return this.hp <= 0;
    }

    public WorldStage getWorld() {
        Stage stage = this.getStage();
        WorldStage world = (stage instanceof WorldStage) ? (WorldStage) stage : null;
        return world;
    }

    public void queueAction(Action action) {
        SequenceAction sequenceAction = null;
        if (getActions().size == 0) {
            sequenceAction = new SequenceAction();
            getActions().add(sequenceAction);
        }

        sequenceAction.addAction(action);
    }

    public void setOrder(Order order) {
        final Order o = order;
//		this.order.setOnStop(new Runnable() {
//			@Override
//			public void run() {
//				Unit.this.order = o;
//				if (Unit.this.order != null) {
//					Unit.this.order.start();
//				}
//			}
//		});
        //this.order.stop();
        this.order = o;
        this.order.start();
    }

    public <T> T getAbility(int index, Class<T> type) {
        return type.cast(abilities[index]);
    }

    public void occupy(Grid grid) {
        grid.addUnit(this);
        grid.state = Grid.State.Blocked;
        currentGrid = grid;
    }

    public void release(Grid grid) {
        grid.removeUnit(this);
        grid.state = Grid.State.Walkable;
    }

    public void occupy(float x, float y) {
        Grid grid = getWorld().getGridMap().grid(x, y);
        occupy(grid);

    }

    public void release(float x, float y) {
        Grid grid = getWorld().getGridMap().grid(x, y);
        release(grid);
    }

    public void handle(Event event) {
//        if (handleAttack(event)) {
//        }
//        else if(handleMoveToPosition(event)) {
//
//        }
//        else if (event.getName() == EventName.MOVE_UNIT) {
//            MoveUnitArgument moveUnitArgument = event.getArgument(MoveUnitArgument.class);
//            if (moveUnitArgument.getUnit() != this) return;
//
//            Actor actor = this.getStage().hit(moveUnitArgument.getTarget().x, moveUnitArgument.getTarget().y, false);
//            if (actor == this) return;
//
//            Unit targetUnit = null;
//            if (actor instanceof Unit) {
//                targetUnit = (Unit) actor;
//            }
//
//            Action action = null;
//
//            if (targetUnit == null) {
//                action = new MoveToPosition(moveUnitArgument.getTarget());
//            } else if (targetUnit.city != null && this.development != null) {
//                Unit city = targetUnit;
//                ParallelAction parallelAction = new ParallelAction(new MoveToUnit(city),
//                        new Develop(city));
//                action = parallelAction;
//            }
//
//            this.clearActions();
//            this.addAction(action);
//        }

    }

    private boolean handleMoveToPosition(Event event) {
        if(!event.is(EventName.MOVE_TO_POSITION)) return false;

        MoveToPositionEvent moveToPositionEvent = event.cast(MoveToPositionEvent.class);

        Action action = new MoveToPosition(moveToPositionEvent.getPosition());
        this.clearActions();
        this.addAction(action);
        return true;
    }

//    private boolean handleAttack(Event event) {
//        if(!event.is(EventName.ATTACK_UNIT)) return false;
//
//        AttackUnitEvent attackUnitEvent = event.cast(AttackUnitEvent.class);
//
//        Unit target = attackUnitEvent.getTarget();
//        Unit attacker = attackUnitEvent.getAttacker();
//        if(attacker!=this) return false;
//        if (target.combat == null || attacker.combat==null) return false;
//
//        SequenceAction sequenceAction = new SequenceAction(new MoveToUnit(target), new com.workasintended.chromaggus.action.Attack(target));
//        RepeatAction repeatAction = new RepeatAction();
//        repeatAction.setAction(sequenceAction);
//        repeatAction.setCount(RepeatAction.FOREVER);
//        Action action = repeatAction;
//        this.clearActions();
//        this.addAction(action);
//
//        return true;
//    }

    private boolean handleDevelop(Event event) {
        if(!(event instanceof DevelopCityEvent)) return false;

        return true;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceToLevelUp() {
        return experienceToLevelUp;
    }

    public void setExperienceToLevelUp(int experienceToLevelUp) {
        this.experienceToLevelUp = experienceToLevelUp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getExperienceGrowth() {
        return experienceGrowth;
    }

    public void setExperienceGrowth(int experienceGrowth) {
        this.experienceGrowth = experienceGrowth;
    }

    public float getAttributeGrowth() {
        return attributeGrowth;
    }

    public void setAttributeGrowth(float attributeGrowth) {
        this.attributeGrowth = attributeGrowth;
    }
}
