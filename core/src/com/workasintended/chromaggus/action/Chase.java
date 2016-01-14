package com.workasintended.chromaggus.action;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.pathfinding.Grid;
import com.workasintended.chromaggus.pathfinding.GridMap;
import com.workasintended.chromaggus.pathfinding.Pathfinder;

import java.util.LinkedList;

/**
 * Created by mazimeng on 8/1/15.
 */
class Chase extends MoveToAction {
    private Grid startGrid;
    private LinkedList<Grid> path = new LinkedList<>();
    private Unit chasing;
    private float speed = 80.0f;
    private float ratio = 32;
    private Pathfinder pathfinder;
    private GridMap gridMap;
    private Grid targetLastGrid;

    @Override
    public boolean act(float delta) {
        Grid nextGrid = path.peekLast();
        if(chasing!=null && nextGrid!=null && nextGrid.state != Grid.State.Walkable) {
            delta = 0;
        }

        return super.act(delta);
    }

    @Override
    protected void begin() {
        super.begin();

        Vector2 currentLocation = new Vector2(startGrid.x * 32, startGrid.y * 32);

        Grid nextGrid = path.peekLast();

        nextGrid.state = Grid.State.Blocked;
        startGrid.state = Grid.State.Walkable;

        Vector2 next = new Vector2(nextGrid.x * 32, nextGrid.y * 32);
        float distance = next.dst(currentLocation);
        float duration = distance / speed;

        setPosition(next.x, next.y);
        setDuration(duration);
    }

    @Override
    protected void end() {
        super.end();

        Grid currentGrid = path.pollLast();
        if (chasing != null) {
            int x = (int) (chasing.getX() / ratio);
            int y = (int) (chasing.getY() / ratio);
            Grid targetGrid = gridMap.grid(x, y);
            LinkedList<Grid> newPath = pathfinder.find(currentGrid, targetGrid);
            path.clear();
            while(newPath.size()>0) {
                path.addFirst(newPath.pollLast());
            }

            if(path.size()>0) {
                getTarget().getActions().add(new Chase());
            }
        }
    }
}