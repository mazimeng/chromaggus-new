package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.workasintended.chromaggus.pathfinding.GridMap;

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

    public Viewport makeWorldViewport() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera cam = makeCamera();
        cam.zoom = 0.4f;

        Viewport viewport = new FillViewport(w, h);
        viewport.setCamera(cam);

        return viewport;
    }
    public Viewport makeGuiViewport() {
        OrthographicCamera cam = makeCamera();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Viewport viewport = new FillViewport(w*0.4f, h*0.4f);
        viewport.setCamera(cam);

        return viewport;
    }

}
