package com.workasintended.chromaggus;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.workasintended.chromaggus.ai.behavior.Blackboard;
import com.workasintended.chromaggus.event.order.MoveToPositionEvent;

/**
 * Created by mazimeng on 6/27/15.
 */
public class Main {
    public static void main(String[] arguments) throws IllegalAccessException, InstantiationException, ReflectionException {
        Sequence<Blackboard> findThreatSequence = new Sequence<Blackboard>(){
            @Override
            public String toString() {
                return "findThreatSequence";
            }
        };
        Task<Blackboard> seq = findThreatSequence.cloneTask();
    }
}
