package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.LinkedList;

public class BlinkSkill extends AbstractSkill {

    private final LinkedList<Location> positions = new LinkedList<>();

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        if (!positions.isEmpty()) {
            Location location = positions.get(RandomUtils.nextInt(positions.size()));
            ParticleHelper.spawnParticleLine(state.getEntity().getLocation(), location, ParticleEffect.PORTAL, null);
            state.getEntity().teleport(location);
            positions.clear();
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        if (positions.size() > 40) {
            positions.removeFirst();
        }
        positions.add(state.getEntity().getLocation());
    }

}
