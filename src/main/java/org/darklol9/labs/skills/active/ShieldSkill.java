package org.darklol9.labs.skills.active;

import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

public class ShieldSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        double missingHealth = state.getEntity().getMaxHealth() - state.getEntity().getHealth();
        state.setShield(missingHealth);
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

    }
}
