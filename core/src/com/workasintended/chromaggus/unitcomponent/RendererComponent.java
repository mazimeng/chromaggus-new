package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.unitcomponent.UnitComponent;

/**
 * Created by mazimeng on 7/11/15.
 */
public class RendererComponent extends UnitComponent {
    public RendererComponent(Unit self) {
        super(self);
    }

    public void draw(Batch batch, float parentAlpha) {
    }
}
