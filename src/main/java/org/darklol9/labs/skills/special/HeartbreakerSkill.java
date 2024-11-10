package org.darklol9.labs.skills.special;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.darklol9.labs.skills.SpecialSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

public class HeartbreakerSkill extends SpecialSkill {

    private double currentReduction = 1.0;

    private long launchTick, resetTick, markerTick;

    private boolean marked;
    private LivingEntity target;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void invoke(BossState state, SkillWrapper skill) {
        if (marked) return;
        target = MobHelper.getNearestPlayer(state.getEntity(), 10);
        if (!(target instanceof Player)) return;
        if (target != null) {
            marked = true;
            launchTick = (long) (System.currentTimeMillis() + (skill.getDoubleOption("launchDelay", 2d) * 1000L));
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        super.tick(time, state, skill);
        if (time >= resetTick && currentReduction != 1.0) {
            currentReduction = 1.0;
        }
        if (marked && time >= launchTick) {
            marked = false;
            if (target != null) {
                if (!skill.getMessage().isEmpty())
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        skill.getMessage().replace("{skill}", skill.getDisplayName())));
                double potentialDefense = MobHelper.calculatePlayerDefense((Player) target);
                target.damage(skill.getDamage(), state.getEntity());
                MobHelper.applyEffect(target, PotionEffectType.SLOW, skill.getDoubleOption("freezePeriod", 3d), 255);
                MobHelper.applyEffect(target, PotionEffectType.JUMP, skill.getDoubleOption("freezePeriod", 3d), 250);
                ParticleHelper.spawnParticleLine(state.getEntity().getLocation(), target.getLocation(), ParticleEffect.HEART, null);
                MobHelper.teleportTo(state.getEntity(), target.getLocation());
                SoundEffect.valueOf(Sound.SKELETON_HURT, 1, 1).run((HumanEntity) target, target.getLocation());
                if (target.getHealth() <= 0) {
                    resetTick = (long) (time + (skill.getDoubleOption("resetDelay", 10d) * 1000L));
                    currentReduction *= potentialDefense;
                    state.heal(0.25 * state.getEntity().getMaxHealth());
                }
                target = null;
            }
        } else if (marked && time >= markerTick) {
            ParticleHelper.spawnParticleRadius(target.getLocation().clone(), 1, ParticleEffect.REDSTONE);
            markerTick = time + 500L;
        }
    }

    @Override
    public double onDamage(BossState state, SkillWrapper skill, double damage) {
        return damage * currentReduction;
    }
}
