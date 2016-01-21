package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by mazimeng on 1/15/16.
 */
public abstract class GameConfiguration {
    public abstract EventListener makeInputListener(WorldStage worldStage, Player player);
    public abstract Viewport makeWorldViewport();
    public abstract Viewport makeGuiViewport();

    protected OrthographicCamera makeCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera cam = new OrthographicCamera(w, h);
        cam.update();
        return cam;
    }
}
