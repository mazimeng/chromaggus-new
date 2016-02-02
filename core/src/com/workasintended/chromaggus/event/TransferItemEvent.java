package com.workasintended.chromaggus.event;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.CityArmory;
import com.workasintended.chromaggus.Event;
import com.workasintended.chromaggus.EventName;

/**
 * Created by mazimeng on 2/2/16.
 */
public class TransferItemEvent extends Event {
    private CityArmory.Item item;
    private Vector2 targetPosition;

    public TransferItemEvent(CityArmory.Item item, Vector2 targetPosition) {
        super(EventName.TRANSFER_ITEM);
        this.item = item;
        this.targetPosition = targetPosition;
    }

    public CityArmory.Item getItem() {
        return item;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }
}
