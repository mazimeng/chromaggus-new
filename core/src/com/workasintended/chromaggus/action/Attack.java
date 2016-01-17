package com.workasintended.chromaggus.action;

import com.badlogic.gdx.math.Vector2;
import com.workasintended.chromaggus.*;
import com.workasintended.chromaggus.event.DebugRendererArgument;

/**
 * Created by mazimeng on 1/16/16.
 */
public class Attack extends MoveToPosition{
    private Unit targetUnit;

    private float attackCooldown = 2;
    private float nextAttack = 2;

    public Attack(Unit targetUnit) {
        super(new Vector2());
        this.targetUnit = targetUnit;
    }

    @Override
    public boolean act(float delta) {
        if((getUnit().getFaction() & targetUnit.getFaction())>0) return true;

        if(targetUnit.dead()) return true;

        if(closeEnough(delta)) {
            nextAttack -= delta;

            if(nextAttack<=0) {
                targetUnit.hp -= getUnit().strength;
                nextAttack = attackCooldown;

                experience(targetUnit.dead());
            }

            return false;
        }
        else {
            nextAttack = attackCooldown;
            return true;
        }
    }

    private void experience(boolean killed) {
        Unit unit = getUnit();
        int experience = unit.getExperience() + unit.getExperienceGrowth() * (killed?2:1);
        if(experience>=unit.getExperienceToLevelUp()) {
            experience = experience - unit.getExperienceToLevelUp();
            unit.setHp((int)(unit.getHp()*unit.getAttributeGrowth()));
            unit.setStrength((int)(unit.getStrength()*unit.getAttributeGrowth()));
        }
        unit.setExperience(experience);
    }

    private boolean closeEnough(float delta) {
        Unit self = getUnit();
        Vector2 nextPosition = new Vector2(self.getX(getAlignment()), self.getY(getAlignment()));
        float distance2 = Vector2.dst2(nextPosition.x, nextPosition.y, targetUnit.getX(getAlignment()), targetUnit.getY(getAlignment()));


        return distance2 <= self.radius*self.radius+targetUnit.radius*targetUnit.radius;
    }

    @Override
    protected boolean completed(float delta) {
        return false;
    }
}
