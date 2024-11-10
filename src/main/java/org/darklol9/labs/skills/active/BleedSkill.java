package org.darklol9.labs.skills.active;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BleedSkill extends AbstractSkill {

    private final Map<Entity, Long> bleeding = new HashMap<>();

    private long lastTick;

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
        for (Entity ent : entities) {
            bleeding.put(ent, System.currentTimeMillis() + (long) (period * 1000));
            if (!skill.getMessage().isEmpty()) ent.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    skill.getMessage().replace("{skill}", skill.getDisplayName())));
        }
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        if (time - lastTick < 1000)
            return;
        lastTick = System.currentTimeMillis();
        bleeding.entrySet().removeIf(e -> e.getValue() <= System.currentTimeMillis());
        bleeding.forEach((key, value) -> {
            LivingEntity ent = (LivingEntity) key;
            ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0),
                    0.5f, 0.5f, 0.5f, 0.1f, 50, ent.getLocation(), 100);
            Vector velocity = ent.getVelocity();
            ent.damage(skill.getDamage(), state.getEntity());
            ent.setVelocity(velocity);
        });
    }

    public boolean isBleeding(Entity ent) {
        return bleeding.containsKey(ent);
    }
}
