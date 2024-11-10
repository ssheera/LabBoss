package org.darklol9.labs.skills;

import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

public abstract class AbstractSkill {

    public abstract boolean isActive();

    public abstract void execute(BossState state, SkillWrapper skill);

    public abstract void tick(long time, BossState state, SkillWrapper skill);

    public double onDamage(BossState state, SkillWrapper skill, double damage) {
        return damage;
    }

    public void onDeath(BossState bossState, SkillWrapper skill) {
    }
}
