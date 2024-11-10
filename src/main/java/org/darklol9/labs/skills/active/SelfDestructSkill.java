package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;
import java.util.stream.Collectors;

public class SelfDestructSkill extends AbstractSkill {

    private int stage, maxStage;
    private long nextTick;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        if (state.isDisableSkills())
            return;
        double wait = skill.getDoubleOption("wait", 5d);
        state.toggleSkills();
        LivingEntity e = state.getEntity();
        MobHelper.setNoAI(e, true);
        maxStage = (int) wait;
        stage = 0;
        double startRadius = skill.getDoubleOption("startRadius", 3d);
        double reducedRadius = skill.getDoubleOption("reducedRadius", 6d);
        double radius = Math.max(startRadius, reducedRadius);
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(e, radius);
        for (Entity ent : entities)
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        if (stage >= maxStage)
            return;
        if (time <= nextTick)
            return;
        nextTick = time + 1000;
        stage++;
        LivingEntity ent = state.getEntity();
        World world = ent.getWorld();
        world.strikeLightningEffect(ent.getLocation());
        if (stage == maxStage) {
            double startRadius = skill.getDoubleOption("startRadius", 3d);
            double reducedRadius = skill.getDoubleOption("reducedRadius", 6d);
            double reducedDamageRate = skill.getDoubleOption("reducedDamageRate", 0.5d);
            ParticleEffect.EXPLOSION_HUGE.display(
                    0.5f, 0.5f, 0.5f, 0.1f, 10, ent.getLocation(), 100);
            MobHelper.getNearbyPlayers(ent, reducedRadius)
                    .forEach(e -> e.damage(skill.getDamage() * reducedDamageRate, ent));
            MobHelper.getNearbyPlayers(ent, startRadius)
                    .forEach(e -> e.damage(skill.getDamage() * (1 - reducedDamageRate), ent));
            state.toggleSkills();
            MobHelper.setNoAI(ent, false);
        }
    }
}
