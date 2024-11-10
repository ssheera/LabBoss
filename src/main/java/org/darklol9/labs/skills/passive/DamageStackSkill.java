package org.darklol9.labs.skills.passive;

import org.apache.commons.lang3.tuple.Pair;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.ArrayList;
import java.util.List;

public class DamageStackSkill extends AbstractSkill {

    private final List<Pair<Double, Long>> damageStack = new ArrayList<>();

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {

    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        double dps = 0;
        for (Pair<Double, Long> pair : damageStack)
            if (time - pair.getRight() < 1000)
                dps += pair.getLeft();
        state.setDps(dps);
        state.setBoostedSkillChance(dps * skill.getDoubleOption("modifier", 0.01));
    }

    @Override
    public double onDamage(BossState state, SkillWrapper skill, double damage) {
        damageStack.removeIf(pair -> System.currentTimeMillis() - pair.getRight() > 1000);
        damageStack.add(Pair.of(damage, System.currentTimeMillis()));
        return damage;
    }
}
