package org.darklol9.labs.skills.special;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.darklol9.labs.skills.SpecialSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.helper.ParticleHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.List;

public class DecimatingSmashSkill extends SpecialSkill {

    private boolean marked;
    private long jumpTick;
    private boolean hasCrashed;
    private boolean running;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        super.execute(state, skill);
    }

    @Override
    public void invoke(BossState state, SkillWrapper skill) {
        if (running) return;
        running = true;
        marked = true;
        hasCrashed = false;
        jumpTick = (long) (System.currentTimeMillis() + (skill.getDoubleOption("jumpDelay", 2d) * 1000L));
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        super.tick(time, state, skill);
        LivingEntity ent = state.getEntity();
        double range = skill.getDoubleOption("size", 5d);
        if (marked && time < jumpTick) {
            for (double r = 0; r < range; r += 0.5)
                ParticleHelper.spawnParticleRadius(ent.getLocation(), r, ParticleEffect.FLAME);
        } else if (marked) {
            ent.setVelocity(new Vector(0, skill.getDoubleOption("jumpForce", 3d), 0));
            marked = false;
        }
        if (running && time >= (jumpTick + 250) && !hasCrashed) {
            if (ent.isOnGround()) {
                hasCrashed = true;
                running = false;
                for (double r = 0; r < range; r += 0.5)
                    createCrashParticle(ent.getLocation(), r);
                List<LivingEntity> players = MobHelper.getNearbyPlayers(ent, range);
                for (LivingEntity player : players) {
                    if (!skill.getMessage().isEmpty()) player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            skill.getMessage().replace("{skill}", skill.getDisplayName())));
                    player.damage(skill.getDamage());
                    player.setVelocity(new Vector(0, skill.getDoubleOption("knockForce", 3d), 0));
                    MobHelper.applyEffect(player, PotionEffectType.SLOW, skill.getDoubleOption("freezePeriod", 3d), 255);
                    MobHelper.applyEffect(player, PotionEffectType.JUMP, skill.getDoubleOption("freezePeriod", 3d), 250);
                }
            }
        }
    }

    private void createCrashParticle(Location location, double radius) {
        ParticleEffect effect = ParticleEffect.BLOCK_CRACK;
        for (int i = 0; i < 360; i += 2) {
            double angle = i * Math.PI / 180;
            double x = MathHelper.cos((float) angle) * radius;
            double z = MathHelper.sin((float) angle) * radius;
            Location loc = location.clone().add(x, 0, z);
            Block under = loc.getBlock().getRelative(BlockFace.DOWN);
            effect.display(new ParticleEffect.BlockData(under.getType(), under.getData()), 0, 0.5f, 0, 0, 5, loc, 100);
        }
    }
}
