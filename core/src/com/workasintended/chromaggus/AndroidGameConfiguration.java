package com.workasintended.chromaggus;

import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Created by mazimeng on 1/15/16.
 */
public class AndroidGameConfiguration extends GameConfiguration {
    @Override
    public EventListener makeInputListener(WorldStage worldStage) {
        return new AndroidInputHandler(worldStage);
    }
}
