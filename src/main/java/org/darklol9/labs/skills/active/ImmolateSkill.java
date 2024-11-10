package org.darklol9.labs.skills.active;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class ImmolateSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        LivingEntity entity = state.getEntity();
        double period = skill.getDoubleOption("period", 3d);
        double size = skill.getDoubleOption("size", 5d);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, size);
        for (LivingEntity ent : entities) {
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
            ent.setFireTicks((int) (period * 20));
            ent.damage(skill.getDamage(), entity);
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {

    }
}
