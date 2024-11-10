package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class PushSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        LivingEntity entity = state.getEntity();
        double distance = skill.getDoubleOption("force", 2d);
        double size = skill.getDoubleOption("size", 3d);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, size);
        for (LivingEntity ent : entities) {
            ent.setVelocity(ent.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(distance));
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
            ParticleEffect.EXPLOSION_LARGE.display(
                    0.5f, 0.5f, 0.5f, 0.1f, 10, ent.getLocation(), 100);
            if (ent instanceof Player)
                ((Player)ent).playSound(ent.getLocation(), Sound.BAT_TAKEOFF, 1f, 1f);
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
    }
}
