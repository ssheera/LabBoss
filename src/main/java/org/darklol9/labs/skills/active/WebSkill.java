package org.darklol9.labs.skills.active;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.SkillWrapper;

import java.util.ArrayList;
import java.util.List;

public class WebSkill extends AbstractSkill {

    private final List<Block> undoBlocks = new ArrayList<>();

    private long lastTick;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(BossState state, SkillWrapper skill) {
        if (!undoBlocks.isEmpty()) return;
        LivingEntity entity = state.getEntity();
        double size = skill.getDoubleOption("size", 5d);
        double radius = state.getBoss().getSkillUsageRadius();
        List<LivingEntity> entities = MobHelper.getNearbyPlayers(entity, radius);
        double closest = Double.MAX_VALUE;
        Entity closestEntity = null;
        for (Entity e : entities) {
            double distance = e.getLocation().distance(entity.getLocation());
            if (distance < closest) {
                closest = distance;
                closestEntity = e;
            }
        }
        if (closestEntity == null)
            return;

        if (!skill.getMessage().isEmpty())
            closestEntity.sendMessage(ChatColor.translateAlternateColorCodes('&',
                skill.getMessage().replace("{skill}", skill.getDisplayName())));

        for (int x = (int) -size; x < size; x++) {
            for (int z = (int) -size; z < size; z++) {
                Block block = closestEntity.getLocation().clone().add(x, 0, z).getBlock();
                if (block.getType() == Material.AIR) {
                    block.setType(Material.WEB);
                    undoBlocks.add(block);
                }
            }
        }
        lastTick = System.currentTimeMillis();
    }

    @Override
    public void tick(long time, BossState state, SkillWrapper skill) {
        if (undoBlocks.isEmpty()) return;
        double period = skill.getDoubleOption("period", 3d);
        if (lastTick + period * 1000 < time) {
            for (Block block : undoBlocks) {
                block.setType(Material.AIR);
            }
            undoBlocks.clear();
        }
    }

    public void removeWebs() {
        for (Block block : undoBlocks)
            block.setType(Material.AIR);
        undoBlocks.clear();
    }

    @Override
    public void onDeath(BossState bossState, SkillWrapper skill) {
        removeWebs();
    }

    public boolean isWeb(Block block) {
        return undoBlocks.contains(block);
    }
}
