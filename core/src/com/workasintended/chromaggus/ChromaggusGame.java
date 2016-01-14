package com.workasintended.chromaggus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ChromaggusGame extends Game {
	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		Service.eventQueue().handle(delta);
		
		super.render();
	}

	public ChromaggusGame() {
		Service.setEventQueue(new DefaultEventQueue());
		Service.setAssetManager(new AssetManager());
	}
	
	@Override
	public void create() {
		WorldScreen screen = new WorldScreen();
		
		this.setScreen(screen);
	}	
}
