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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.workasintended.chromaggus.*;
import com.workasintended.chromaggus.ability.Fireball;
import com.workasintended.chromaggus.ability.Melee;
import com.workasintended.chromaggus.ai.StateDefense;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;
import com.workasintended.chromaggus.unitcomponent.*;

import java.util.Iterator;

public class Episode01 {
	private Skin skin;
	public Unit lead1;
	public Unit lead2;

	public Episode01(Skin skin) {
		this.skin = skin;
	}
	public void build(WorldStage stage, Table guiLayout) {
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

		BitmapFont font = new BitmapFont();
		font.getData().setScale(0.5f, 0.5f);

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
				Unit city = this.makeCity(stage, font, new TextureRegion(textureCity), Faction.FACTION_A);
				city.setPosition(14*32, 25*32);
				stage.addActor(city);
				makeCityArmory(city);
			}
			stage.getGridMap().grid(8, 7).state= Grid.State.Blocked;

			{
				TextureRegion[] frames = new TextureRegion[2];
				frames[0] = char01Frames[0][0];
				frames[1] = char01Frames[0][2];

				Unit unit = makeCharacter(stage, Faction.FACTION_A, font, frames);
				unit.setPosition(470, 760);
				stage.addActor(unit);
				lead1 = unit;
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[0][3];
				frames[1] = char00Frames[0][5];
				Unit unit = makeCharacter(stage, Faction.FACTION_A, font, frames);
				unit.setPosition(400, 815);
				stage.addActor(unit);
				lead2 = unit;
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[4][0];
				frames[1] = char00Frames[4][2];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(210, 720);
				unit.ai = new StateDefense(unit, stage);
				unit.combat.setStrength(8);
				unit.combat.setPrimaryWeapon(makeFireball());
				stage.addActor(unit);
			}

			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[4][3];
				frames[1] = char00Frames[4][5];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(768, 470);
				unit.combat.setStrength(12);
				unit.ai = new StateDefense(unit, stage);
				stage.addActor(unit);
			}
			{
				TextureRegion[] frames = new TextureRegion[2];

				frames[0] = char00Frames[0][6];
				frames[1] = char00Frames[0][8];
				Unit unit = makeCharacter(stage, Faction.FACTION_B, font, frames);
				unit.setPosition(880, 380);
				unit.combat.setStrength(15);
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

		{

			TiledMap map = new TmxMapLoader(new InternalFileHandleResolver()).load("episode01.tmx");
			float unitScale = 2f;
			OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, unitScale, stage.getBatch());
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

	protected Unit makeCharacter(WorldStage stage, Faction faction, BitmapFont font, TextureRegion[] frames) {
		Animation animation = new Animation(0.5f, frames);
		Unit unit = new Unit();

		CharacterRendererComponent characterRendererComponent = new CharacterRendererComponent(unit);
		characterRendererComponent.setAnimation(animation);

		unit.renderer = characterRendererComponent;

		unit.setTouchable(Touchable.enabled);
		unit.setFont(font);

		unit.setSize(32, 32);
		unit.setFaction(faction);

		MovementComponent movementComponent = new MovementComponent(unit);

        DevelopmentComponent developmentComponent = new DevelopmentComponent(unit);
        unit.development = developmentComponent;
		unit.movement = movementComponent;
		unit.combat = new CombatComponent(unit);
        unit.dialogComponent = new DialogComponent(unit, new Label("", skin));
		unit.inventory = new CityArmory.Inventory(2);
		unit.inventory.addListener(new EquipEventListener(unit));
		return unit;
	}

	protected void makeCityArmory(Unit city) {
		int slots = 3;

		CityArmory cityArmory = new CityArmory(slots, skin);
		city.city.setArmory(cityArmory);

		{
			Melee melee = new Melee();
			melee.setPower(1.0f);
			Weapon weapon = makeSword(ActorFactory.instance().icon()[9][3], melee);
			cityArmory.addCraft(weapon);

		}
		{
			Melee melee = new Melee();
			melee.setPower(2f);
			Weapon weapon = makeSword(ActorFactory.instance().icon()[24][5], melee);
			cityArmory.addCraft(weapon);

		}
		cityArmory.addCraft(makeFireball());
	}

	private Weapon makeSword(TextureRegion icon, Melee melee) {
		Weapon sword = new Weapon(new TextureRegionDrawable(icon), melee);
		return sword;
	}
	private Weapon makeFireball() {
		Weapon fireball = new Weapon(new TextureRegionDrawable(ActorFactory.instance().icon()[6][0]), new Fireball());
		return fireball;
	}


	protected Unit makeCity(WorldStage stage, BitmapFont font, TextureRegion texture, Faction faction) {
		Sprite sprite = new Sprite(texture);
		sprite.setSize(32, 32);


		Unit unitCity = new Unit();
		unitCity.setTouchable(Touchable.enabled);
		unitCity.setFaction(faction);

        CityComponent component = new CityComponent(unitCity);
        unitCity.city = component;
        unitCity.setSprite(sprite);
        return unitCity;
	}
}
