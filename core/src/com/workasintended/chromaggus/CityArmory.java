package com.workasintended.chromaggus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.workasintended.chromaggus.action.Wait;
import com.workasintended.chromaggus.event.ShowCityWeaponEvent;

import java.util.List;

/**
 * Created by mazimeng on 1/30/16.
 */
public class CityArmory extends Table implements EventHandler{
    private Cell[] weaponSlots;
    private InventorySlot[] inventory;

    public CityArmory(int slotCount, Skin skin) {
        super();
        this.defaults().size(32);
        this.weaponSlots = new Cell[slotCount];

        for(int i=0; i<weaponSlots.length; ++i) {
//            final Craft craft = new Craft(skin){
//                @Override
//                protected void craftComplete() {
//                    super.craftComplete();
//                    addItem(this.getItem());
//                }
//            };
//
//            craft.addListener(new ClickListener(){
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    super.clicked(event, x, y);
//                    System.out.println(String.format("weapon clicked: target(%s)", craft.getUserObject()));
//                    craft.startCrafting(1);
//                }
//            });

            Cell slot = this.add();
//            slot.setActor(craft);
            weaponSlots[i] = slot;
        }

        initInventory();
    }

    public void addCraft(Item item) {

    }

    private void addItem(Image item) {
        for (InventorySlot inventorySlot : inventory) {
            if(inventorySlot.isEmpty()) {
                inventorySlot.setDrawable(item.getDrawable());
                inventorySlot.setEmpty(false);
                break;
            }
        }
    }
    private void initInventory() {
        int slotCount = 6;
        inventory = new InventorySlot[slotCount];
        this.row();
        for(int i=0; i<slotCount; ++i) {
            final InventorySlot inventorySlot = new InventorySlot();
            this.add(inventorySlot);
            inventory[i] = inventorySlot;

            final DragAndDrop dragAndDrop = new DragAndDrop();
			dragAndDrop.addSource(new DragAndDrop.Source(inventorySlot) {
				@Override
				public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
					DragAndDrop.Payload payload = new DragAndDrop.Payload();
					payload.setDragActor(inventorySlot);
					//dragAndDrop.setDragActorPosition(-inventorySlot.getWidth()*0.5f, inventorySlot.getHeight()*0.5f);
					return payload;
				}

				@Override
				public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
					super.dragStop(event, x, y, pointer, payload, target);
//					Vector2 vec2 = GuiStage.this.stageToScreenCoordinates(new Vector2(event.getStageX(), event.getStageY()));
//					vec2 = worldStage.screenToStageCoordinates(vec2);
//					Actor actor = worldStage.hit(vec2.x, vec2.y, true);
				}
			});
        }
    }


    @Override
    public void handle(Event event) {
        if(event.is(EventName.SHOW_CITY_WEAPON)) {
//            ShowCityWeaponEvent showCityWeaponEvent = event.cast();
//            boolean show = showCityWeaponEvent.isShow();
//            List<Craft> crafts = showCityWeaponEvent.getCity().city.getCrafts();
//            int i=0;
//            for (Craft craft : crafts) {
//                Cell slot = weaponSlots[i];
//                slot.setActor(craft);
//                if(i>=weaponSlots.length) break;
//                ++i;
//            }
        }
    }

    public static class InventorySlot extends Image {
        private boolean empty = true;

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    public static class Item extends Image {
        public Item clone() {
            return new Item();
        }
    }

    public static class Craft extends Stack {
        private Image item;
        private Label progressLabel;

        public Craft(Image image, Skin skin) {
            this.item = image;
            this.progressLabel = new Label("", skin);

            this.add(item);
            this.add(progressLabel);
        }

        public void setItem(Image item) {
            this.item.setDrawable(item.getDrawable());
        }

        public Label getProgressLabel() {
            return progressLabel;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
        }

        public void startCrafting(final float seconds) {
            if(this.getActions().size>0) return;

            Wait wait = new Wait(seconds){
                private float remaining = seconds;
                @Override
                public boolean act(float delta) {
                    boolean done = super.act(delta);

                    remaining-=delta;
                    updateProgress(remaining);
                    if(done) {
                        craftComplete();
                    }
                    return done;
                }
            };

            this.addAction(wait);
        }

        public void updateProgress(float secondsRemaining) {
            if(secondsRemaining>0) {
                this.progressLabel.setText(String.format("%.1f", secondsRemaining));
            }
            else {
                this.progressLabel.setText("");
            }
        }

        protected void craftComplete() {

        }

        public Image getItem() {
            return item;
        }
    }
}
