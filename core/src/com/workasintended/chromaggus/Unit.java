package com.workasintended.chromaggus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.ability.Melee;
import com.workasintended.chromaggus.action.AttackTarget;
import com.workasintended.chromaggus.action.MoveToPosition;
import com.workasintended.chromaggus.action.MoveToUnit;
import com.workasintended.chromaggus.ai.AiComponent;
import com.workasintended.chromaggus.event.MoveUnitArgument;
import com.workasintended.chromaggus.order.Attack;
import com.workasintended.chromaggus.order.Idle;
import com.workasintended.chromaggus.order.Move;
import com.workasintended.chromaggus.order.Order;
import com.workasintended.chromaggus.pathfinding.Grid;

import java.util.LinkedList;
import java.util.List;

public class Unit extends Group implements EventHandler{
	public int hp;
	public int strength;
	public float radius = 32;
	public float speed = 32;
	private int faction = 0;
	
	public Sprite highlight;
	
	private Sprite sprite;
	private boolean highlighted = false;
	
	private BitmapFont font;	
	
	private GameComponent[] components = new GameComponent[10];

	public boolean selected = false;
	public AiComponent ai;
	public CombatComponent combat;
	public CityComponent city;
	public MovementComponent movement;

	private Order order = new Idle();

	public Ability[] abilities;

	public Grid currentGrid;

	private LinkedList<UnitTask> taskQueue = new LinkedList<>();
	private UnitTask executingTask = null;

	public Unit() {
		abilities = new Ability[9];
		abilities[Ability.MELEE] = new Melee(this);

		
		this.addListener(new InputListener(){

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
		if(this.dead()) {
			this.remove();
			Service.eventQueue().enqueue(new Event(EventName.UNIT_DIED, this));
		}
		super.act(delta);
		
		if(this.city!=null) this.city.update(delta);
		if(this.ai != null) this.ai.update(delta);
		if(this.combat != null) this.combat.update(delta);

		if(this.order!=null) this.order.update(delta);

		for(Ability ability: abilities) {
			if(ability == null) continue;
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
	public void draw (Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);		
		if(this.sprite != null) batch.draw(sprite
				, this.getX(), this.getY()
				, this.getOriginX(), this.getOriginY()
				, this.getWidth(), this.getHeight()
				, this.getScaleX(), this.getScaleY()
				, this.getRotation());
		
		if(font!=null) font.draw(batch, Integer.toString(this.hp), this.getX(), this.getY());
		if(this.city!=null) this.city.draw(batch);
		
		if(this.highlight != null) batch.draw(highlight
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
		WorldStage world = (stage instanceof WorldStage)?(WorldStage)stage:null;
		return world;
	}

	public void queueAction(Action action) {
		SequenceAction sequenceAction = null;
		if(getActions().size==0) {
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

	@Override
	public void handle(Event event) {
		if(event.getName() == EventName.MOVE_UNIT) {
			MoveUnitArgument moveUnitArgument = event.getArgument(MoveUnitArgument.class);
            if(moveUnitArgument.getUnit() != this) return;

            Actor actor = this.getStage().hit(moveUnitArgument.getTarget().x, moveUnitArgument.getTarget().y, false);
			if(actor == this) return;

            boolean attack = actor instanceof Unit;

            Action action = null;
            if(attack) {
				SequenceAction sequenceAction = new SequenceAction(new MoveToUnit((Unit)actor), new com.workasintended.chromaggus.action.Attack((Unit)actor));
				RepeatAction repeatAction = new RepeatAction();
				repeatAction.setAction(sequenceAction);
				repeatAction.setCount(RepeatAction.FOREVER);
				action = repeatAction;
            }
            else {
                action = new MoveToPosition(moveUnitArgument.getTarget());
            }
            this.clearActions();
            this.addAction(action);


//			if(moveUnitArgument.getUnit() == this) {
//				if (this.movement == null) return;
//
//				System.out.println("handling MOVE_UNIT");
//
//				Actor actor = this.getStage().hit(moveUnitArgument.getTarget().x, moveUnitArgument.getTarget().y, false);
//				boolean chase = actor instanceof Unit;
//
//				if(chase) {
//					this.setOrder(new Attack(this, (Unit)actor));
//				}
//				else {
//					this.setOrder(new Move(this, moveUnitArgument.getTarget()));
//				}
//			}

			return;
		}

	}
}
