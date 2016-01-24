package com.workasintended.chromaggus.unitcomponent;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.workasintended.chromaggus.Service;
import com.workasintended.chromaggus.Unit;
import com.workasintended.chromaggus.ability.Ability;
import com.workasintended.chromaggus.action.MoveToUnit;
import com.workasintended.chromaggus.action.UseAbility;
import com.workasintended.chromaggus.event.TakeDamageEvent;

public class CombatComponent extends UnitComponent {
    private Ability[] abilities = new Ability[9];

    private int level = 1;
    private int hp = 100;
    private int maxHp = 100;
    private int strength = 5;
    private int intelligence = 5;
    private float radius = 32;
    private int experience = 0;
    private int experienceToLevelUp = 100;
    private float attributeGrowth = 1.2f;
    private float experienceGrowthFromAttack = 0.2f;
    private float experienceGrowthFromKill = 0.8f;

    private Ability primaryAbility;


    public CombatComponent(Unit self) {
        super(self);
    }

    @Override
    public void update(float delta) {
        if (primaryAbility != null) primaryAbility.update(delta);
    }

    protected void updateAbilities(float delta) {
        for (Ability ability : this.abilities) {
            if (ability == null) continue;
            ability.update(delta);
        }
    }

    public void attack(Unit target) {
        if (target == getSelf()) return;
        if (primaryAbility == null) return;

        primaryAbility.setTarget(target);
        primaryAbility.setUser(getSelf());

        UseAbility useAbility = new UseAbility(primaryAbility);

//        RepeatAction repeatAttack = new RepeatAction();
//        repeatAttack.setAction(new Attack(getSelf(), target));
//        repeatAttack.setCount(RepeatAction.FOREVER);

        SequenceAction sequenceAction = new SequenceAction(
                new MoveToUnit(target, getSelf().getSpeed(), primaryAbility.getCastRange()),
                useAbility);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);

        getSelf().clearActions();
        getSelf().addAction(repeatAction);
    }

    public void takeDamage(int damage) {
        this.setHp(this.getHp() - damage);

        Service.eventQueue().enqueue(new TakeDamageEvent(this.getSelf()));
    }


    public void setAbility(int index, Ability ability) {
        this.abilities[index] = ability;
    }

    public Ability getAbility(int index) {
        return this.abilities[index];
    }

    public void gainExperienceFromAttack() {
        gainExperience((int) (experienceGrowthFromAttack * experienceToLevelUp));
    }

    public void gainExperienceFromKill() {
        gainExperience((int) (experienceGrowthFromKill * experienceToLevelUp));
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        this.levelup();
        System.out.println(String.format("exp: %s, %s, %s, hp(%s), str(%s), int(%s)",
                this.getSelf().hashCode(), this.level, this.experience, this.maxHp, this.strength,
                this.intelligence));
    }

    public boolean levelup() {
        int nextLevel = this.experience / this.experienceToLevelUp + 1;

        if (nextLevel <= this.level) return false;

        while (this.level < nextLevel) {
            this.level++;
            this.maxHp = (int) (this.maxHp * this.attributeGrowth);
            this.strength = (int) (this.strength * this.attributeGrowth);
            this.intelligence = (int) (this.intelligence * this.attributeGrowth);
        }
        return true;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = MathUtils.clamp(hp, 0, maxHp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceToLevelUp() {
        return experienceToLevelUp;
    }

    public void setExperienceToLevelUp(int experienceToLevelUp) {
        this.experienceToLevelUp = experienceToLevelUp;
    }

    public float getAttributeGrowth() {
        return attributeGrowth;
    }

    public void setAttributeGrowth(float attributeGrowth) {
        this.attributeGrowth = attributeGrowth;
    }

    public float getExperienceGrowthFromAttack() {
        return experienceGrowthFromAttack;
    }

    public void setExperienceGrowthFromAttack(float experienceGrowthFromAttack) {
        this.experienceGrowthFromAttack = experienceGrowthFromAttack;
    }

    public float getExperienceGrowthFromKill() {
        return experienceGrowthFromKill;
    }

    public void setExperienceGrowthFromKill(float experienceGrowthFromKill) {
        this.experienceGrowthFromKill = experienceGrowthFromKill;
    }

    @Override
    public String toString() {
        return "CombatComponent{" +
                "level=" + level +
                ", hp=" + hp +
                ", maxHp=" + maxHp +
                ", strength=" + strength +
                ", experience=" + experience +
                ", experienceToLevelUp=" + experienceToLevelUp +
                ", attributeGrowth=" + attributeGrowth +
                ", experienceGrowthFromAttack=" + experienceGrowthFromAttack +
                ", experienceGrowthFromKill=" + experienceGrowthFromKill +
                ", radius=" + radius +
                '}';
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setPrimaryAbility(Ability primaryAbility) {
        this.primaryAbility = primaryAbility;
    }

    public Ability getPrimaryAbility() {
        return primaryAbility;
    }
}
