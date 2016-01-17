package com.workasintended.chromaggus.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.workasintended.chromaggus.Unit;

/**
 * Created by mazimeng on 1/17/16.
 */
public class Develop extends Action {
    private Unit city;

    public Develop(Unit city) {
        this.city = city;
    }

    @Override
    public boolean act(float delta) {
        if(this.city.city == null) return true;


        return false;
    }
}
