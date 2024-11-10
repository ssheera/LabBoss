package org.darklol9.labs.skills.active;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class PullSkill extends AbstractSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        LivingEntity entity = state.getEntity();
        double size = skill.getDoubleOption("size", 3d);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, size);
        for (LivingEntity ent : entities) {
            ent.setVelocity(ent.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(-1));
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
            if (ent instanceof Player)
                ((Player)ent).playSound(ent.getLocation(), Sound.BAT_HURT, 1, 1);
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
    }
}
