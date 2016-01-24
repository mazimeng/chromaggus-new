package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.unitcomponent.RendererComponent;

/**
 * Created by mazimeng on 7/11/15.
 */
public class CharacterRendererComponent extends RendererComponent {
    float stateTime;
    Animation animation;
    private float blink = 0;

    public CharacterRendererComponent(Unit self) {
        super(self);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Unit unit = getSelf();
        stateTime += Gdx.graphics.getDeltaTime();
        float delta = Gdx.graphics.getDeltaTime();
        TextureRegion frame = animation.getKeyFrame(stateTime, true);


        if(blink > 0) {
            blink = Math.max(blink - delta*30, 0);

            if(blink == 0) {
                batch.setColor(1, 1, 1, 1.0f);
            }
            else {
                batch.setColor(1, 1, 1, (float)(Math.cos(blink) +1)/2.0f);
            }
        }


        batch.draw(frame
                , unit.getX(), unit.getY()
                , unit.getOriginX(), unit.getOriginY()
                , unit.getWidth(), unit.getHeight()
                , unit.getScaleX(), unit.getScaleY()
                , unit.getRotation());

        super.draw(batch, parentAlpha);

        if(blink > 0) batch.setColor(1, 1, 1, 1.0f);
    }

    public void blink() {
        blink = (float)Math.PI * 4;
    }

    @Override
    public TextureRegion getIcon() {
        return animation.getKeyFrame(0);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
