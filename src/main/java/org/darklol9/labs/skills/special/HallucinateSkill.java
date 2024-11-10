package org.darklol9.labs.skills.special;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.skills.SpawningSpecialSkill;
import org.darklol9.labs.skills.SpecialSkill;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HallucinateSkill extends SpawningSpecialSkill {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void invoke(BossState state, SkillWrapper skill) {
        if (System.currentTimeMillis() <= removeTicks + 1000)
            return;
        Boss boss = state.getBoss();
        MiniBoss minion =
                MiniBoss
                        .builder()
                        .id("hallucinate-" + boss.getId())
                        .displayName(boss.getDisplayName())
                        .entityType(boss.getEntityType())
                        .maxHealth(skill.getDoubleOption("healthRatio", 0.25) * boss.getMaxHealth())
                        .damageReduction(skill.getDoubleOption("resistanceRatio", 0.5) * boss.getDamageReduction())
                        .attackDamage(skill.getDoubleOption("damageRatio", 0.7) * boss.getAttackDamage())
                        .attackSpeed(boss.getAttackSpeed())
                        .helmet(boss.getHelmet())
                        .chestplate(boss.getChestplate())
                        .leggings(boss.getLeggings())
                        .boots(boss.getBoots())
                        .mainHand(boss.getMainHand())
                        .effects(boss.getEffects())
                        .build();
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
