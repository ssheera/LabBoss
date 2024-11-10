package org.darklol9.labs.skills.passive;

import org.apache.commons.lang3.tuple.Pair;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.ArrayList;
import java.util.List;

public class VampSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {

    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

    }

    @Override
    public double onDamage(BossState state, SkillWrapper skill, double damage) {
        state.heal(damage * skill.getDoubleOption("modifier", 0.01));
        return damage;
    }
}
