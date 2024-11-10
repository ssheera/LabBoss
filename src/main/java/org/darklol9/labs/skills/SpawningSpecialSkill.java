package org.darklol9.labs.skills;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.HashMap;
import java.util.Map;

public abstract class SpawningSpecialSkill extends SpecialSkill {

    protected final Map<LivingEntity, MiniBoss> minions = new HashMap<>();
    protected long removeTicks = 0;

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        super.tick(time, state, skill);
        if (!minions.isEmpty() && System.currentTimeMillis() > removeTicks) {
            removeMinions();
        }
    }

    public Map<LivingEntity, MiniBoss> getMinions() {
        return minions;
    }

    public MiniBoss get(LivingEntity entity) {
        for (Map.Entry<LivingEntity, MiniBoss> entry : minions.entrySet()) {
            Entity minion = entry.getKey();
            if (minion == null || !minion.isValid()) continue;
            World world = minion.getWorld();
            if (world == null) continue;
            if (!world.equals(entity.getWorld())) continue;
            if (minion.getEntityId() == entity.getEntityId())
                return entry.getValue();
        }
        return null;
    }

    public void removeMinions() {
        minions.keySet().forEach(LivingEntity::remove);
        minions.clear();
    }

    @Override
    public void onDeath(BossState bossState, SkillWrapper skill) {
        removeMinions();
    }
}
