package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class RejuvenateSkill extends AbstractSkill {

    private double heal, totalHeal;
    private long lastTick = 0;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        LivingEntity entity = state.getEntity();
        double ratio = skill.getDoubleOption("ratio", 0.25d);
        double duration = skill.getDoubleOption("duration", 5000d);
        heal = ratio / (duration / 1000);
        totalHeal = ratio * entity.getMaxHealth();
        double radius = skill.getDoubleOption("radius", 5d);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, radius);
        entities.forEach(e -> {
            if (!skill.getMessage().isEmpty()) e.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
        });
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

        if (totalHeal <= 0) return;
        if (time - lastTick <= 1000) return;

        lastTick = time;

        LivingEntity entity = state.getEntity();

        double amt = heal * entity.getMaxHealth();

        totalHeal -= amt;

        state.heal(amt);
        ParticleEffect.HEART.display(entity.getLocation(), 0.5f, (float) (entity.getEyeHeight() + 0.5d), 0.5f, 0.1f, 10);
    }
}
