package com.workasintended.chromaggus.episode;

import com.badlogic.gdx.Gdx;
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
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.ability.Melee;
import com.workasintended.chromaggus.ai.StateDefense;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;

import java.util.Iterator;

public class Episode01 {
	public void build(WorldStage stage) {
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

		BitmapFont font = new BitmapFont();

		Texture textureCity = new Texture("city.png");
		Texture textureCursor = new Texture("cursor.png");
		Texture char00 = new Texture("char00.png");
		Texture char01 = new Texture("char01.png");
		Texture char02 = new Texture("char02.png");

		TextureRegion[][] char01Frames = TextureRegion.split(char01,
				char01.getWidth() / 3, char01.getHeight() / 4);
		TextureRegion[][] char00Frames = TextureRegion.split(char00,
				char00.getWidth() / 12, char00.getHeight() / 8);
		TextureRegion[][] char02Frames = TextureRegion.split(char02,
				char00.getWidth() / 12, char00.getHeight() / 8);
		textureCity = textureCursor;

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
				frames[0] = char01Frames[0][0];
				frames[1] = char01Frames[0][2];

				Unit unit = makeCharacter(stage, Faction.FACTION_A, font, frames);
				unit.setPosition(470, 760);
				stage.addActor(unit);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[0][3];
				frames[1] = char00Frames[0][5];
				Unit unit = makeCharacter(stage, Faction.FACTION_A, font, frames);
				unit.setPosition(400, 815);
				stage.addActor(unit);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[4][0];
				frames[1] = char00Frames[4][2];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(210, 720);
				unit.ai = new StateDefense(unit, stage);
				unit.setStrength(8);
				stage.addActor(unit);
			}

			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[4][3];
				frames[1] = char00Frames[4][5];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(768, 470);
				unit.setStrength(12);
				unit.ai = new StateDefense(unit, stage);
				stage.addActor(unit);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[0][6];
				frames[1] = char00Frames[0][8];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(880, 380);
				unit.setStrength(15);
				unit.ai = new StateDefense(unit, stage);
				stage.addActor(unit);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char02Frames[0][0];
				frames[1] = char02Frames[0][2];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(870, 320);
				unit.ai = new StateDefense(unit, stage);
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
        unit.strength = 5;
        unit.setFont(font);
        unit.combat = new CombatComponent();
        unit.setFaction(faction);

        unit.setSprite(sprite);
        return unit;
	}

	protected Unit makeCharacter(WorldStage stage, int faction, BitmapFont font, TextureRegion[] frames) {
		Animation animation = new Animation(0.5f, frames);
		Unit unit = new Unit();

		CharacterRendererComponent characterRendererComponent = new CharacterRendererComponent(unit);
		characterRendererComponent.setAnimation(animation);

		unit.renderer = characterRendererComponent;

		unit.setTouchable(Touchable.enabled);
		unit.hp = 100;
		unit.strength = 5;
		unit.setFont(font);
		unit.combat = new CombatComponent();
		unit.setSize(32, 32);
		unit.setFaction(faction);

		MovementComponent movementComponent = new MovementComponent(stage.getGridMap());
		movementComponent.setUnit(unit);

        DevelopmentComponent developmentComponent = new DevelopmentComponent(unit);
        unit.development = developmentComponent;

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
