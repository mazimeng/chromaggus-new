package com.workasintended.chromaggus;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by mazimeng on 1/15/16.
 */
public abstract class GameConfiguration {
    public abstract EventListener makeInputListener(WorldStage worldStage);
    public abstract EventHandler makeInputHandler();
    public abstract Camera makeCamera();
}
