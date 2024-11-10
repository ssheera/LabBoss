package org.darklol9.labs.skills.active;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class BlindSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        LivingEntity entity = state.getEntity();
        double size = skill.getDoubleOption("size", 5d);
        double period = skill.getDoubleOption("period", 5d);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, size);
        for (Entity ent : entities) {
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
            MobHelper.applyEffect(ent, PotionEffectType.BLINDNESS, period, 3);
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

    }
}
