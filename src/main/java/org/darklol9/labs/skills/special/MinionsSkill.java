package org.darklol9.labs.skills.special;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.skills.SpawningSpecialSkill;
import org.darklol9.labs.skills.SpecialSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinionsSkill extends SpawningSpecialSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void invoke(BossState state, SkillWrapper skill) {
        if (System.currentTimeMillis() <= removeTicks + 1000)
            return;
        String minionId = skill.getOption("minion", "");
        MiniBoss minion = MConf.get().minions.stream().filter(m -> m.getId().equalsIgnoreCase(minionId)).findFirst().orElse(null);
        if (minion == null)
            return;
        int spawnAmount = skill.getIntOption("spawnAmount", 1);
        double removeTime = skill.getDoubleOption("removeTime", 10);
        removeTicks = System.currentTimeMillis() + (long) (removeTime * 1000);
        int nearbyRadius = skill.getIntOption("nearbyRadius", 10);
        List<LivingEntity> nearbyPlayers = MobHelper.getNearbyPlayers(state.getEntity(), nearbyRadius);
        if (!nearbyPlayers.isEmpty()) {
            for (LivingEntity nearbyPlayer : nearbyPlayers) {
                if (!skill.getMessage().isEmpty())
                    nearbyPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            skill.getMessage().replace("{skill}", skill.getDisplayName())));
            }
            for (int i = 0; i < spawnAmount; i++) {
                LivingEntity entity = minion.spawn(state.getEntity().getLocation());
                minions.put(entity, minion);
            }
        }
    }
}
