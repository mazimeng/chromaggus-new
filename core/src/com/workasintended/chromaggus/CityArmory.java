package com.workasintended.chromaggus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.workasintended.chromaggus.action.Wait;
import com.workasintended.chromaggus.event.ShowCityWeaponEvent;
import com.workasintended.chromaggus.event.TransferItemEvent;

import java.util.List;

/**
 * Created by mazimeng on 1/30/16.
 */
public class CityArmory extends Table implements EventHandler{
    private Craft[] crafts;
    private Inventory inventory;

    public CityArmory(int slotCount, Skin skin) {
        super();
        this.defaults().size(32);
        this.crafts = new Craft[slotCount];

        for(int i=0; i<slotCount; ++i) {
            Craft craft = new Craft(skin){
                @Override
                protected void craftComplete() {
                    inventory.putItem(this.getItem().clone());
                }
            };
            crafts[i] = craft;
            this.add(craft);
        }

        initInventory();
    }

    public void addCraft(Item item) {
        for (Craft craft : this.crafts) {
            if(craft.getItem()==null) {
                craft.setItem(item);
                break;
            }
        }
    }

    private void initInventory() {
        int slotCount = 6;
        inventory = new Inventory(slotCount);
        this.row();
        this.add(inventory);
//        this.row();
//        for(int i=0; i<slotCount; ++i) {
//            final InventorySlot inventorySlot = new InventorySlot();
//            this.add(inventorySlot);
//            inventory[i] = inventorySlot;
//
//            final DragAndDrop dragAndDrop = new DragAndDrop();
//			dragAndDrop.addSource(new DragAndDrop.Source(inventorySlot) {
//				@Override
//				public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
//					DragAndDrop.Payload payload = new DragAndDrop.Payload();
//					payload.setDragActor(inventorySlot);
//					//dragAndDrop.setDragActorPosition(-inventorySlot.getWidth()*0.5f, inventorySlot.getHeight()*0.5f);
//					return payload;
//				}
//
//				@Override
//				public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
//					super.dragStop(event, x, y, pointer, payload, target);
////					Vector2 vec2 = GuiStage.this.stageToScreenCoordinates(new Vector2(event.getStageX(), event.getStageY()));
////					vec2 = worldStage.screenToStageCoordinates(vec2);
////					Actor actor = worldStage.hit(vec2.x, vec2.y, true);
//				}
//			});
//        }
    }


    @Override
    public void handle(Event event) {

    }

    public static class InventorySlot extends Container {
        public InventorySlot() {
            super();
            this.setTouchable(Touchable.enabled);
        }
    }

    public static class Inventory extends Table {
        private InventorySlot[] slots;
        public Inventory(int slotCount) {
            super();
            this.slots = new InventorySlot[slotCount];
            for(int i=0; i<slotCount; ++i) {
                InventorySlot slot = new InventorySlot();
                this.add(slot);
                this.slots[i] = slot;
            }
        }

        public void putItem(Item item) {
            for (InventorySlot slot : slots) {
                if(slot.getActor()==null) {
                    slot.setActor(item);
                    break;
                }
            }
        }
    }

    public static class Item extends Image {
        private DragAndDrop dragAndDrop = new DragAndDrop();
        public Item(Drawable drawable) {
            super(drawable);
            dragAndDrop.addSource(new DragAndDrop.Source(this) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(new Image(Item.this.getDrawable()));
                    //dragAndDrop.setDragActorPosition(-inventorySlot.getWidth()*0.5f, inventorySlot.getHeight()*0.5f);
                    return payload;
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                    super.dragStop(event, x, y, pointer, payload, target);
//					Vector2 vec2 = GuiStage.this.stageToScreenCoordinates(new Vector2(event.getStageX(), event.getStageY()));
//					vec2 = worldStage.screenToStageCoordinates(vec2);
                    Item item = (Item)getActor();
                    TransferItemEvent transferItemEvent = new TransferItemEvent(item, new Vector2(event.getStageX(), event.getStageY()));
                    Service.eventQueue().enqueue(transferItemEvent);
                }
            });
        }
        public Item clone() {
            return new Item(this.getDrawable());
        }
    }

    public static class Craft extends Stack {
        private Item item;
        private Label progressLabel;
        private float craftTime = 2;

        private Container itemContainer = new Container();

        public Craft(Skin skin) {
            this.progressLabel = new Label("", skin);

            this.add(itemContainer);
            this.add(progressLabel);

            this.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Craft.this.startCrafting();
                }
            });
        }

        public void setItem(Item item) {
            this.item = item;
            itemContainer.setActor(item);
        }

        public Item getItem() {
            return this.item;
        }

        public Label getProgressLabel() {
            return progressLabel;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
        }

        public void startCrafting() {
            if(this.getActions().size>0) return;

            Wait wait = new Wait(craftTime){
                private float remaining = craftTime;
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
    }
}
