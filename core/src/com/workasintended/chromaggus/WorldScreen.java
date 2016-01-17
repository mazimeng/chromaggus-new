package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.*;
import com.workasintended.chromaggus.episode.Episode01;
import com.workasintended.chromaggus.pathfinding.GridMap;

public class WorldScreen implements Screen {
	private WorldStage stage;
	private GuiStage gui;
	private EventListener inputHandler;
	private GameConfiguration gameConfiguration;

	public WorldScreen(GameConfiguration gameConfiguration) {
		this.gameConfiguration = gameConfiguration;

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

	protected void initAssets() {
		Service.assetManager().load("icon.png", Texture.class);
		Service.assetManager().finishLoading();
	}

	protected void initWorld() {
		Camera cam = gameConfiguration.makeCamera();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		FillViewport viewport = new FillViewport(w*0.4f, h*0.4f);
		viewport.setCamera(cam);

		stage = new WorldStage();
		stage.setGridMap(new GridMap(100));
		stage.setShapeRenderer(new ShapeRenderer());
//		stage.getViewport().setCamera(cam);
		stage.setViewport(viewport);

		this.inputHandler = this.gameConfiguration.makeInputListener(stage);
		stage.addListener(this.inputHandler);


		new Episode01().build(stage);
	}
	
	protected void initGui() {
		Camera cam = gameConfiguration.makeCamera();

		gui = new GuiStage();
		gui.setWorldStage(stage);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		FitViewport viewport = new FitViewport(w*0.4f, h*0.4f);
		viewport.setCamera(cam);

		gui.setViewport(viewport);
	}

	protected void initInputs() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(gui);
		multiplexer.addProcessor(stage);

		Gdx.input.setInputProcessor(multiplexer);
	}

	protected void initEvents() {
		{
			CommandHandler ch = new CommandHandler(stage);
			Service.eventQueue().register(EventName.MOVE, ch);
			Service.eventQueue().register(EventName.MOVE_UNIT, stage);
			Service.eventQueue().register(EventName.SELECTION_STARTED, stage);
			Service.eventQueue().register(EventName.SELECTING, stage);
			Service.eventQueue().register(EventName.SELECTION_COMPLETED, stage);
			Service.eventQueue().register(EventName.MOVING_CAMERA, stage);
			Service.eventQueue().register(EventName.RENDER_ANIMATION, stage);
			Service.eventQueue().register(EventName.UNIT_DIED, stage);
			Service.eventQueue().register(EventName.SET_DEBUG_RENDERER, stage);
		}

		Service.eventQueue().register(EventName.CANCEL_SELECTION, gameConfiguration.makeInputHandler());
		Service.eventQueue().register(EventName.SELECTION_COMPLETED, gui);

	}

	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setWorldSize(width*0.4f, height*0.4f);
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
