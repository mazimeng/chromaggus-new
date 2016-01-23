package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.viewport.*;
import com.workasintended.chromaggus.episode.Episode01;
import com.workasintended.chromaggus.pathfinding.GridMap;

public class WorldScreen implements Screen {
	private WorldStage stage;
	private GuiStage gui;
	private EventListener inputHandler;
	private GameConfiguration gameConfiguration;
	private Player player = new Player();

	public WorldScreen(GameConfiguration gameConfiguration) {
		this.gameConfiguration = gameConfiguration;
		this.initPlayer();
		this.initAssets();
		this.initWorld();
		this.initGui();
		this.initInputs();
		this.initEvents();
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
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

	protected void initAssets() {
		Service.assetManager().load("icon.png", Texture.class);
		Service.assetManager().finishLoading();
	}

	protected void initWorld() {
		Viewport viewport = gameConfiguration.makeWorldViewport();

		stage = new WorldStage();
		stage.setGridMap(new GridMap(100));
		stage.setShapeRenderer(new ShapeRenderer());
		stage.setViewport(viewport);

		this.inputHandler = this.gameConfiguration.makeInputListener(stage, player);
		stage.addListener(this.inputHandler);


		new Episode01().build(stage);
	}
	
	protected void initGui() {
		Viewport viewport = gameConfiguration.makeGuiViewport();

		gui = new GuiStage();
		gui.setWorldStage(stage);
		gui.setViewport(viewport);
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

		Service.eventQueue().register(EventName.SELECTION_COMPLETED, gui);
		Service.eventQueue().register(EventName.UNIT_SELECTED, gui);
		Service.eventQueue().register(EventName.UNIT_DESELECTED, gui);
		Service.eventQueue().register(EventName.GAIN_GOLD, player);
		Service.eventQueue().register(EventName.BUY_ITEM, player);
		Service.eventQueue().register(EventName.USE_ABILITY, player);

	}

	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setWorldSize(width, height);
		stage.getViewport().update(width, height);

		gui.getViewport().setWorldSize(width*0.4f, height*0.4f);
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
