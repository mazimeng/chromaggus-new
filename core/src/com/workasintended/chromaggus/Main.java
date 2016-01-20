package com.workasintended.chromaggus;

import com.workasintended.chromaggus.event.order.MoveToPositionEvent;

/**
 * Created by mazimeng on 6/27/15.
 */
public class Main {
    public static void main(String[] arguments) {
        MoveToPositionEvent moveToPositionEvent = new MoveToPositionEvent(null, null);
        Class<?> a = MoveToPositionEvent.class;
        System.out.println(a.isInstance(moveToPositionEvent));
    }
}
