package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by mazimeng on 1/15/16.
 */
public class AndroidGameConfiguration extends GameConfiguration {
    private AndroidInputHandler androidInputHandler;
    @Override
    public EventListener makeInputListener(WorldStage worldStage) {
        this.androidInputHandler = new AndroidInputHandler(worldStage);
        return androidInputHandler;
    }

    @Override
    public EventHandler makeInputHandler() {
        return androidInputHandler;
    }

    @Override
    public Camera makeCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera cam = new OrthographicCamera(h, w);
        cam.update();
        return cam;
    }
}
