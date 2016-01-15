package com.workasintended.chromaggus.episode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.workasintended.chromaggus.*;
import com.workasintended.chromaggus.Character;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.ability.Melee;
import com.workasintended.chromaggus.ai.Ai;
import com.workasintended.chromaggus.ai.AiComponent;
import com.workasintended.chromaggus.ai.StateDevelop;
import com.workasintended.chromaggus.ai.StateIdle;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;

import java.util.Iterator;
import java.util.Random;

public class Episode01 {
	public void build(WorldStage stage) {
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

		BitmapFont font = new BitmapFont();

		Texture unit1 = new Texture("red.png");
		Texture unit2 = new Texture("blue.png");
		Texture textureCity = new Texture("city.png");
		Sprite circle = new Sprite(new Texture("circle.png"));
		Texture textureCursor = new Texture("cursor.png");
		Texture characterSpriteSheet = new Texture("characters.png");
		Texture char01 = new Texture("char01.png");

		TextureRegion[][] characterFrames = TextureRegion.split(characterSpriteSheet,
				characterSpriteSheet.getWidth() / 16, characterSpriteSheet.getHeight() / 10);


		textureCity = textureCursor;

//		Ai ai = new Ai();
//		stage.addActor(ai);

		{
			{
				Unit city = this.makeCity(stage, font, new TextureRegion(textureCity));
				city.setPosition(14*32, 25*32);
				stage.getGridMap().grid(14, 25).state = Grid.State.Blocked;
				stage.addActor(city);
			}
			stage.getGridMap().grid(8, 7).state= Grid.State.Blocked;

			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = characterFrames[0][0];
				frames[1] = characterFrames[0][1];

				Character unit = makeCharacter(stage, 1, font, characterSpriteSheet, frames);
				unit.setPosition(12*32, 22*32);

				Texture slashSpriteSheet = new Texture("slash.png");
				TextureRegion[][] tmp = TextureRegion.split(slashSpriteSheet,
						slashSpriteSheet.getWidth() / 4, slashSpriteSheet.getHeight() / 1);
				TextureRegion[] slashFrames = new TextureRegion[4];

				slashFrames[0] = tmp[0][0];
				slashFrames[1] = tmp[0][1];
				slashFrames[2] = tmp[0][2];
				slashFrames[3] = tmp[0][3];
				Animation animation = new Animation(0.5f, slashFrames);

				Melee melee = unit.getAbility(Ability.MELEE, Melee.class);
				melee.setAnimationRenderable(new AnimationRenderable(animation));

				stage.addActor(unit);
				Grid grid = stage.getGridMap().grid(unit.getX(), unit.getY());
				unit.occupy(grid);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = characterFrames[3][0];
				frames[1] = characterFrames[3][1];
				Character unit = makeCharacter(stage, 1, font, characterSpriteSheet, frames);
				unit.setPosition(10 * 32, 20 * 32);

				stage.addActor(unit);
				Grid grid = stage.getGridMap().grid(unit.getX(), unit.getY());
				unit.occupy(grid);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = characterFrames[2][0];
				frames[1] = characterFrames[2][1];
				Character unit = makeCharacter(stage, 2, font, characterSpriteSheet, frames);
				unit.setPosition(7 * 32, 24 * 32);
				Grid grid = stage.getGridMap().grid(unit.getX(), unit.getY());
				unit.occupy(grid);
				unit.ai = new AiComponent();
				unit.ai.setAiState(new StateIdle(unit));
				stage.addActor(unit);
			}

		}

//		{
//			Unit city = this.makeCity(stage, font, textureCity);
//			city.setPosition(4*32, 4*32);
//			stage.addActor(city);
//			stage.getGridMap().grid(4, 4).state= Grid.State.Blocked;
//
//			{
//				Unit unit = this.makeCharacter(2, font, unit2);
//				unit.ai = new StateDevelop(city, unit);
//				unit.setPosition(12*32, 4*32);
//				stage.addActor(unit);
//
//			}
//		}

		{
//			Cursor cursor = new Cursor(textureCursor);
//			stage.addListener(new CursorListener(cursor));
//			stage.addActor(cursor);
		}

//        stage.addListener(new HumanController(stage));

		{
			TiledMap map = new TmxMapLoader(new InternalFileHandleResolver()).load("episode01.tmx");
			float unitScale = 2f;
			OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, unitScale);
			stage.setTiledMapRenderer(renderer);

			TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
			MapObjects objects = layer.getObjects();
			layer.getCell(8, 25).getTile().getProperties();
			GridMap gridMap = stage.getGridMap();
			for(int i=0; i<layer.getHeight(); ++i) {
				for(int j=0; j<layer.getWidth(); ++j) {
					TiledMapTileLayer.Cell cell = layer.getCell(j, i);
					MapProperties mp = cell.getTile().getProperties();
					Iterator<String> it = mp.getKeys();
					if(mp.containsKey("state")) {
						String value = mp.get("state").toString();
						if(value.equals("blocked")) {
							Grid grid = gridMap.grid(j, i);
							grid.state = Grid.State.Blocked;
						}
					}
				}
			}
		}

		stage.getCamera().position.set(12*32f, 24*32f, 0);

	}

	protected Unit makeCharacter(int faction, BitmapFont font, Texture texture) {
		Sprite sprite = new Sprite(texture);

        Unit unit = new Unit();
        unit.setTouchable(Touchable.enabled);
        unit.hp = 100;
        unit.strength = 50;
        unit.setFont(font);
        unit.combat = new CombatComponent();
        unit.setFaction(faction);

        unit.setSprite(sprite);
        return unit;
	}

	protected Character makeCharacter(WorldStage stage, int faction, BitmapFont font, Texture spriteSheet, TextureRegion[] frames) {
		Animation animation = new Animation(0.5f, frames);
		Character unit = new Character();
		unit.setAnimation(animation);
		unit.setTouchable(Touchable.enabled);
		unit.hp = 100;
		unit.strength = 50;
		unit.setFont(font);
		unit.combat = new CombatComponent();
		unit.setSize(32, 32);
		unit.setFaction(faction);

		MovementComponent movementComponent = new MovementComponent(stage.getGridMap());
		movementComponent.setUnit(unit);

		unit.movement = movementComponent;
		return unit;
	}

	protected Unit makeCity(WorldStage stage, BitmapFont font, TextureRegion texture) {
		Sprite sprite = new Sprite(texture);
		sprite.setSize(32, 32);


		Unit unitCity = new Unit();
		unitCity.setTouchable(Touchable.enabled);
		unitCity.hp = 10;
		unitCity.strength = 1;
		unitCity.setFaction(3);

        CityComponent component = new CityComponent(stage, unitCity, font);
        unitCity.city = component;
        unitCity.setSprite(sprite);
        return unitCity;
	}
}
