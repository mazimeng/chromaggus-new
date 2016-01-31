package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.*;
import com.workasintended.chromaggus.action.Do;
import com.workasintended.chromaggus.action.Wait;
import com.workasintended.chromaggus.episode.Episode01;
import com.workasintended.chromaggus.event.BuyItemEvent;
import com.workasintended.chromaggus.pathfinding.GridMap;

public class WorldScreen implements Screen {
	private WorldStage stage;
	private GuiStage gui;
	private EventListener inputHandler;
	private GameConfiguration gameConfiguration;
	private Player player = new Player();
	private UnitSelection unitSelection;
    private Skin skin;

    private Cell cityCraft;

	public WorldScreen(GameConfiguration gameConfiguration) {
		this.gameConfiguration = gameConfiguration;

        skin = gameConfiguration.makeSkin();

		this.initPlayer();
		this.initWorld();
		this.initGui();
		this.initInputs();
		this.initEvents();
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		if(stage != null) {
			stage.act(delta);

			stage.getViewport().apply();
			stage.draw();
		}

		gui.act(Gdx.graphics.getDeltaTime());

		gui.getViewport().apply();
		gui.draw();
	}

	protected void initPlayer() {
		this.player = new Player();
		this.player.setFaction(Faction.FACTION_A);
	}

	protected void initWorld() {
		Viewport viewport = gameConfiguration.makeWorldViewport();

		stage = new WorldStage();
		stage.setGridMap(new GridMap(100));
		stage.setShapeRenderer(new ShapeRenderer());
		stage.setViewport(viewport);

		this.inputHandler = this.gameConfiguration.makeInputListener(stage, player);
		stage.addListener(this.inputHandler);


        final OrthographicCamera cam = (OrthographicCamera) stage.getCamera();
		final Episode01 e01 = new Episode01(skin);
        e01.build(stage);

        SequenceAction sequenceAction = new SequenceAction();
        {
            sequenceAction.addAction(new Wait(2));

            sequenceAction.addAction(new Do(new Runnable() {
                @Override
                public void run() {
                    cam.position.x = e01.lead1.getX(Align.center);
                    cam.position.y = e01.lead1.getY(Align.center);
                    e01.lead1.dialogComponent.say("hi");
                }
            }));
            sequenceAction.addAction(new Wait(2));
            sequenceAction.addAction(new Do(new Runnable() {
                @Override
                public void run() {
                    e01.lead1.dialogComponent.say("bye");
                }
            }));

        }

        {
            sequenceAction.addAction(new Wait(2));

            sequenceAction.addAction(new Do(new Runnable() {
                @Override
                public void run() {
                    cam.position.x = e01.lead2.getX(Align.center);
                    cam.position.y = e01.lead2.getY(Align.center);
                    e01.lead2.dialogComponent.say("hi");
                }
            }));
            sequenceAction.addAction(new Wait(2));
            sequenceAction.addAction(new Do(new Runnable() {
                @Override
                public void run() {
                    e01.lead2.dialogComponent.say("bye");
                }
            }));

        }
        //stage.addAction(sequenceAction);
	}

	protected void makeCityWeapon(Table table, Unit city) {
		int slots = 3;
        cityCraft = table.add();
        city.city.setCityWeapon(new CityArmory(slots, skin));
	}

	protected void initGui() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
		Viewport viewport = gameConfiguration.makeGuiViewport();

		gui = new GuiStage();
		gui.setViewport(viewport);

		TextureRegion[][] icons = ActorFactory.instance().icon();

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table().right().bottom();
		//table.defaults().size(32, 32);
		table.setFillParent(true);

		initCityWeapon(table);

		table.row();

		gui.addActor(table);


		{
			unitSelection = new UnitSelection(new TextureRegionDrawable(icons[1][0]));
			table.add(unitSelection);
//			stack.add(unitSelection);
		}

		{
			TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(icons[6][0]);
			final ImageButton imageButton  = new ImageButton(textureRegionDrawable);
			ImageButton invalid = new ImageButton(new TextureRegionDrawable(icons[3][9]));
			ImageButton valid = new ImageButton(new TextureRegionDrawable(icons[1][0]));
			valid.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Service.eventQueue().enqueue(new Event(EventName.CANCEL_SELECTION));
				}
			});

			imageButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Service.eventQueue().enqueue(new BuyItemEvent());
				}
			});


			table.add(imageButton);
			table.add(invalid);
			table.add(valid);

			/**
			 * these are drag&drop example.
			 * leave them be for now
			 */
//			final DragAndDrop dragAndDrop = new DragAndDrop();
//			dragAndDrop.addSource(new DragAndDrop.Source(imageButton) {
//				@Override
//				public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
//					DragAndDrop.Payload payload = new DragAndDrop.Payload();
//					payload.setDragActor(new ImageButton(imageButton.getImage().getDrawable()));
//					dragAndDrop.setDragActorPosition(-imageButton.getWidth()*0.5f, imageButton.getHeight()*0.5f);
//					return payload;
//				}
//
//				@Override
//				public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
//					super.dragStop(event, x, y, pointer, payload, target);
//					Vector2 vec2 = GuiStage.this.stageToScreenCoordinates(new Vector2(event.getStageX(), event.getStageY()));
//					vec2 = worldStage.screenToStageCoordinates(vec2);
//					Actor actor = worldStage.hit(vec2.x, vec2.y, true);
//				}
//			});
		}

		table.setDebug(true);

	}

	protected void initInputs() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(gui);
		multiplexer.addProcessor(stage);

		Gdx.input.setInputProcessor(multiplexer);
	}

	protected void initEvents() {
		Service.eventQueue().register(EventName.MOVE_UNIT, stage);
		Service.eventQueue().register(EventName.SELECTION_STARTED, stage);
		Service.eventQueue().register(EventName.SELECTING, stage);
		Service.eventQueue().register(EventName.SELECTION_COMPLETED, stage);
		Service.eventQueue().register(EventName.MOVING_CAMERA, stage);
		Service.eventQueue().register(EventName.RENDER_ANIMATION, stage);
		Service.eventQueue().register(EventName.UNIT_DIED, stage);
		Service.eventQueue().register(EventName.SET_DEBUG_RENDERER, stage);
		Service.eventQueue().register(EventName.ATTACK_UNIT, stage);
		Service.eventQueue().register(EventName.MOVE_TO_POSITION, stage);
		Service.eventQueue().register(EventName.FOLLOW_UNIT, stage);
		Service.eventQueue().register(EventName.UNIT_SELECTED, stage);
		Service.eventQueue().register(EventName.UNIT_SELECTED, player);
		Service.eventQueue().register(EventName.UNIT_DESELECTED, player);
		Service.eventQueue().register(EventName.DEVELOP_CITY, stage);

		Service.eventQueue().register(EventName.SELECTION_COMPLETED, unitSelection);
		Service.eventQueue().register(EventName.UNIT_SELECTED, unitSelection);
		Service.eventQueue().register(EventName.UNIT_DESELECTED, unitSelection);
		Service.eventQueue().register(EventName.GAIN_GOLD, player);
		Service.eventQueue().register(EventName.BUY_ITEM, player);
		Service.eventQueue().register(EventName.USE_ABILITY, player);
		Service.eventQueue().register(EventName.TAKE_DAMAGE, new RenderHandler());
		Service.eventQueue().register(EventName.TAKE_DAMAGE, stage);
		Service.eventQueue().register(EventName.SHOW_CITY_WEAPON, cityWeapon);

	}

	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setWorldSize(width, height);
		stage.getViewport().update(width, height);

        float zoom = 1280*0.4f/width;
		gui.getViewport().setWorldSize(width*zoom, height*zoom);
		gui.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void setInputHandler(EventListener inputHandler) {
		this.inputHandler = inputHandler;
	}

	public WorldStage getStage() {
		return stage;
	}

	public void setStage(WorldStage stage) {
		this.stage = stage;
	}

	public GuiStage getGui() {
		return gui;
	}

	public void setGui(GuiStage gui) {
		this.gui = gui;
	}

	public EventListener getInputHandler() {
		return inputHandler;
	}
}
