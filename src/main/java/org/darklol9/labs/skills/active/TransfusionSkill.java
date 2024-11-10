package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.skills.SpecialSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class TransfusionSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        List<LivingEntity> nearby = MobHelper.getNearbyPlayers(state.getEntity(), skill.getDoubleOption("size", 4d));
        double damageDealt = 0;
        for (LivingEntity player : nearby) {
            player.damage(skill.getDamage(), state.getEntity());
            damageDealt += skill.getDamage();
            ParticleHelper.spawnParticleLine(state.getEntity().getLocation(), player.getLocation(), ParticleEffect.REDSTONE, null);
            if (!skill.getMessage().isEmpty()) player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
        }
        state.heal(skill.getDoubleOption("healRatio", 0.75d) * damageDealt);
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

    }
}
