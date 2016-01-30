package com.workasintended.chromaggus;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.workasintended.chromaggus.event.ShowCityWeaponEvent;

import java.util.List;

/**
 * Created by mazimeng on 1/30/16.
 */
public class CityWeapon implements EventHandler{
    private ImageButton[] weaponSlots;

    public CityWeapon(int slotCount) {
        this.weaponSlots = new ImageButton[slotCount];
    }

    public ImageButton[] getWeaponSlots() {
        return weaponSlots;
    }

    @Override
    public void handle(Event event) {
        if(event.is(EventName.SHOW_CITY_WEAPON)) {
            ShowCityWeaponEvent showCityWeaponEvent = event.cast();
            boolean show = showCityWeaponEvent.isShow();
            List<Weapon> weapons = showCityWeaponEvent.getCity().city.getWeapons();
            int i=0;
            for (Weapon weapon : weapons) {
                ImageButton weaponSlot = weaponSlots[i];
                weaponSlot.getStyle().imageUp = new TextureRegionDrawable(weapon.getIcon());
                weaponSlot.setUserObject(weapon);

                ++i;
                if(i>=weaponSlots.length) break;
            }
        }
    }
}
