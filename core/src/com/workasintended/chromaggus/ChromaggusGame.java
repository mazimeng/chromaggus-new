package com.workasintended.chromaggus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class ChromaggusGame extends Game {
	private GameConfiguration gameConfiguration;

	public ChromaggusGame(GameConfiguration gameConfiguration) {
		Service.setEventQueue(new DefaultEventQueue());
		Service.setAssetManager(new AssetManager());

		this.gameConfiguration = gameConfiguration;
	}

	@Override
	public void create() {
		EventListener inputListener = this.gameConfiguration.makeInputListener();
		WorldScreen screen = new WorldScreen(inputListener);
		
		this.setScreen(screen);
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		Service.eventQueue().handle(delta);

		super.render();
	}

	public void setGameConfiguration(GameConfiguration gameConfiguration) {
		this.gameConfiguration = gameConfiguration;
	}
}
