package org.darklol9.labs.struct;

import com.golfing8.winespigot.WineSpigotConfig;
import com.massivecraft.massivecore.ps.PS;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.utils.helper.MobHelper;

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class BossSpawner implements IScheduler {

    private long ticks;
    private String bossId;
    private PS location;
    private transient Random random = new Random();

    private transient long lastCheck = System.currentTimeMillis();

    private transient LinkedList<BossState> spawnedBosses = new LinkedList<>();

    public BossSpawner(Boss boss, PS location) {
        this.bossId = boss.getId();
        this.location = location;
        resetTicks();
    }

    public Location getLocation() {
        return location.asBukkitLocation(true);
    }

    public Block getBlock() {
        return location.asBukkitBlock(true);
    }

    public boolean canSpawn() {
        if (spawnedBosses.size() >= MConf.get().maxNearbySameBoss) return false;
        return MobHelper.getNearbyPlayers(getLocation(), MConf.get().minSpawnNearbyRadius).size() > 0;
    }

    public void tick() {
        if (random == null) random = new Random();
        Location location = getLocation();
        World nmsWorld = ((org.bukkit.craftbukkit.v1_8_R3.CraftWorld) location.getWorld()).getHandle();
        if (!WineSpigotConfig.disableNearbyPlayerSpawnerParticles) {
            double d1 = location.getX() + random.nextFloat();
            double d2 = location.getY() + random.nextFloat();
            double d0 = location.getZ() + random.nextFloat();
            nmsWorld.addParticle(EnumParticle.SMOKE_NORMAL, d1, d2, d0, 0.0, 0.0, 0.0);
            nmsWorld.addParticle(EnumParticle.FLAME, d1, d2, d0, 0.0, 0.0, 0.0);
        }
        if (!canSpawn()) return;
        ticks--;
        if (ticks <= 0) {
            resetTicks();
            spawnBoss();
        }
    }

    private void resetTicks() {
        ticks = ThreadLocalRandom.current().nextInt(MConf.get().minSpawnDelay, MConf.get().maxSpawnDelay + 1);
    }

    private void spawnBoss() {
        if (random == null) random = new Random();
        Location location = getLocation();
        if (!WineSpigotConfig.disableSpawnerSpawnParticles) {
            net.minecraft.server.v1_8_R3.World nmsWorld = ((org.bukkit.craftbukkit.v1_8_R3.CraftWorld) getLocation().getWorld()).getHandle();
            nmsWorld.triggerEffect(2004, new BlockPosition(location.getX(), location.getY(), location.getZ()), 0);
        }
        int roomToSpawn = MConf.get().maxNearbySameBoss - spawnedBosses.size();
        int amountToSpawn = ThreadLocalRandom.current().nextInt(MConf.get().minSpawnAmount, MConf.get().maxSpawnAmount + 1);
        if (amountToSpawn > roomToSpawn) amountToSpawn = roomToSpawn;
        for (int i = 0; i < amountToSpawn; i++) {
            double d0 = location.getX() + (random.nextDouble() - random.nextDouble()) * MConf.get().spawnerSpawnAreaRadius + 0.5;
            double d3 = location.getY() + random.nextInt(3) - 1;
            double d4 = location.getZ() + (random.nextDouble() - random.nextDouble()) * MConf.get().spawnerSpawnAreaRadius + 0.5;
            float f0 = random.nextFloat() * 360.0F;
            float f1 = 0.0F;
            Location spawnLocation = new Location(location.getWorld(), d0, d3, d4, f0, f1);
            Boss boss = MConf.get().bosses.stream().filter(b -> b.getId().equals(bossId)).findFirst().orElse(null);
            if (boss == null) return;
            boss.spawn(this, spawnLocation);
        }
    }

    @Override
    public void spawn(BossState bossState) {
        if (spawnedBosses == null) spawnedBosses = new LinkedList<>();
        spawnedBosses.add(bossState);
    }

    @Override
    public void death(BossState bossState) {
        if (spawnedBosses == null) spawnedBosses = new LinkedList<>();
        spawnedBosses.remove(bossState);
    }

    @Override
    public void checkBossStates() {
        if (System.currentTimeMillis() - lastCheck <= 10000) return;
        lastCheck = System.currentTimeMillis();
        if (spawnedBosses == null) spawnedBosses = new LinkedList<>();
        spawnedBosses.removeIf(bs -> !bs.isAlive());
    }

}
