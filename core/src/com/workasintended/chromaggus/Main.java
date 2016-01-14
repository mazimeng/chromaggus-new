package com.workasintended.chromaggus;

import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;
import com.workasintended.chromaggus.pathfinding.Pathfinder;

import java.util.LinkedList;

/**
 * Created by mazimeng on 6/27/15.
 */
public class Main {
    public static void main(String[] arguments) {
        GridMap map = new GridMap(10);
        map.grid(3, 2).state = Grid.State.Blocked;
        map.grid(3, 3).state = Grid.State.Blocked;
        map.grid(3, 4).state = Grid.State.Blocked;

        Grid start = map.grid(1,3);
        Grid end = map.grid(5,3);

        Pathfinder pathfinder = new Pathfinder(map);
        LinkedList<Grid> path = pathfinder.find(start, end, map);


        while(!path.isEmpty()) {
            Grid grid = path.pop();
        }
    }
}
