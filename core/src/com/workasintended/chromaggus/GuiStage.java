package com.workasintended.chromaggus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.workasintended.chromaggus.event.SelectionCompleted;
import com.workasintended.chromaggus.event.UnitSelectedEvent;

import java.util.List;

/**
 * Created by mazimeng on 8/8/15.
 */
public class GuiStage extends Stage implements EventHandler {
    private Skin skin;
    private CityPanel cityPanel = new CityPanel();
    private WorldStage worldStage;

    private UnitSelection unitSelection;

    public GuiStage() {
        this.initSkin();
        this.initGui();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 coord = this.worldStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        Actor actor = worldStage.hit(screenX, screenY, false);

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof SelectionCompleted) {
            List<Unit> units = ((SelectionCompleted) event).getUnits();

            if(units.size() == 0 || units.get(0).city==null) return;

            Unit unit = units.get(0);
            cityPanel.gold.setText(unit.city.getGold().toString());
        }
        else {
            this.unitSelection.handle(event);
        }


    }

    protected void initGui() {
        Texture itemTexture = Service.assetManager().get("icon.png");
        TextureRegion[][] icons = TextureRegion.split(itemTexture,
                itemTexture.getWidth() / 16, itemTexture.getHeight() / 39);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table().right().bottom();
        table.defaults().size(32, 32);
        table.setFillParent(true);
        this.addActor(table);

        {
            unitSelection = new UnitSelection(new TextureRegionDrawable(icons[1][0]));
            table.add(unitSelection);
        }

        {
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(icons[9][3]);
            final ImageButton imageButton  = new ImageButton(textureRegionDrawable);
            ImageButton invalid = new ImageButton(new TextureRegionDrawable(icons[0][1]));
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
                    super.clicked(event, x, y);
                }
            });

            table.row();
            table.add(imageButton);
            table.add(invalid);
            table.add(valid);

            final DragAndDrop dragAndDrop = new DragAndDrop();
            dragAndDrop.addSource(new DragAndDrop.Source(imageButton) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(new ImageButton(imageButton.getImage().getDrawable()));
                    dragAndDrop.setDragActorPosition(-imageButton.getWidth()*0.5f, imageButton.getHeight()*0.5f);
                    return payload;
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                    super.dragStop(event, x, y, pointer, payload, target);
                    Vector2 vec2 = GuiStage.this.stageToScreenCoordinates(new Vector2(event.getStageX(), event.getStageY()));
                    vec2 = worldStage.screenToStageCoordinates(vec2);
                    Actor actor = worldStage.hit(vec2.x, vec2.y, false);
                }
            });
        }

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Click me!", skin);
//        table.row();
        //table.add(button);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                button.setText("Good job!");
            }
        });

        // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
        //table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
        table.setDebug(true);
    }

    void initSkin() {
// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle listStyle = new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle();
        listStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        listStyle.font = skin.getFont("default");
        listStyle.fontColorSelected = Color.GREEN;
        listStyle.fontColorUnselected = Color.GRAY;
//        listStyle.selection = Color.BLUE;
//        skin.add("default", listStyle);
    }

    void initIcons() {

    }

    public WorldStage getWorldStage() {
        return worldStage;
    }

    public void setWorldStage(WorldStage worldStage) {
        this.worldStage = worldStage;
    }


    class CityPanel {
        public Label development;
        public Label gold;
    }

    public static class UnitSelection extends Image implements EventHandler{
        private Drawable empty;
        public UnitSelection(Drawable empty) {
            super(empty);
            this.empty = empty;
        }

        @Override
        public void handle(Event event) {
            UnitSelectedEvent unitSelectedEvent = event.cast(UnitSelectedEvent.class);
            if(unitSelectedEvent!=null) {
                Unit unit = unitSelectedEvent.getUnit();
                if(unit.renderer==null) return;

                TextureRegion icon = unit.renderer.getIcon();
                this.setDrawable(new TextureRegionDrawable(icon));
            }
        }
    }
}
