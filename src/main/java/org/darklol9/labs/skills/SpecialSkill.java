package org.darklol9.labs.skills;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.entity.LivingEntity;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

public abstract class SpecialSkill extends AbstractSkill {

    private long tickToInvoke;
    private boolean waitingForInvoke;

    private long markerTick;

    public abstract void invoke(BossState state, SkillWrapper skill);

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        if (waitingForInvoke) return;
        tickToInvoke = System.currentTimeMillis() + 1000L;
        waitingForInvoke = true;
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        if (!waitingForInvoke) return;
        if (time >= tickToInvoke) {
            invoke(state, skill);
            waitingForInvoke = false;
        } else {
            if (time >= markerTick) {
                LivingEntity target = state.getEntity();
                for (double i = 0; i < 2; i += 0.5) {
                    ParticleHelper.spawnParticleRadius(target.getLocation().clone().add(0, i, 0), 1, ParticleEffect.ENCHANTMENT_TABLE);
                }
                markerTick = time + 500L;
            }
        }
    }
}
