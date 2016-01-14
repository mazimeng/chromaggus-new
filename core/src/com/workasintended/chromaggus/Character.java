package com.workasintended.chromaggus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by mazimeng on 7/11/15.
 */
public class Character extends Unit {
    float stateTime;
    Animation animation;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        batch.draw(frame
                , this.getX(), this.getY()
                , this.getOriginX(), this.getOriginY()
                , this.getWidth(), this.getHeight()
                , this.getScaleX(), this.getScaleY()
                , this.getRotation());
        super.draw(batch, parentAlpha);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
