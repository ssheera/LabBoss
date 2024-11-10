package org.darklol9.labs.utils.helper;

import com.massivecraft.massivecore.PotionEffectWrap;
import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.darklol9.labs.nms.pathfinding.PathfinderGoalGoBack;
import org.darklol9.labs.nms.pathfinding.PathfinderGoalPassiveMeleeAttack;
import org.darklol9.labs.struct.BossState;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class MobHelper {

    public void clearGoals(LivingEntity entity) {
        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
        if (nmsEntity instanceof EntityInsentient) {
            EntityInsentient insentient = (EntityInsentient) nmsEntity;
            insentient.goalSelector = new PathfinderGoalSelector(insentient.world != null && insentient.world.methodProfiler != null ? insentient.world.methodProfiler : null);
            insentient.targetSelector = new PathfinderGoalSelector(insentient.world != null && insentient.world.methodProfiler != null ? insentient.world.methodProfiler : null);
        }
    }

    private void markAsForcedLoad(LivingEntity entity) {
        EntityLiving living = ((CraftLivingEntity) entity).getHandle();
        living.loadChunks = true;
        if (living instanceof EntityInsentient) {
            ((EntityInsentient) living).persistent = true;
        }
    }

    public void setupEntity(LivingEntity entity, double attackDamage, double attackSpeed, double rangedAttackRange) {
        markAsForcedLoad(entity);
        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
        if (nmsEntity instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) nmsEntity;

            creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35.0);

            clearGoals(entity);
            creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
            if (nmsEntity instanceof IRangedEntity) {
                creature.goalSelector.a(1, new PathfinderGoalArrowAttack((IRangedEntity) creature, 1.25,
                        (int) attackSpeed, (float) rangedAttackRange));
            } else {
                creature.goalSelector.a(1, new PathfinderGoalPassiveMeleeAttack(creature, EntityLiving.class, 1.0, false, attackSpeed, attackDamage));
            }
            creature.goalSelector.a(4, new PathfinderGoalRandomStroll(creature, 1.0));
            creature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(creature, 1.0));
            creature.goalSelector.a(8, new PathfinderGoalLookAtPlayer(creature, EntityLiving.class, 8.0F));
            creature.goalSelector.a(8, new PathfinderGoalRandomLookaround(creature));
            creature.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(creature, EntityLiving.class, true));
            creature.targetSelector.a(2, new PathfinderGoalHurtByTarget(creature, false));
        }
    }

    public void setSpawnPathfinder(BossState boss) {
        Location spawn = boss.getEntity().getLocation();
        EntityLiving nmsEntity = ((CraftLivingEntity) boss.getEntity()).getHandle();
        if (nmsEntity instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) nmsEntity;
            creature.goalSelector.a(6, new PathfinderGoalGoBack(creature, boss, spawn.getX(), spawn.getY(), spawn.getZ()));
        }
    }

    public void setNoAI(Entity entity, boolean state) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", state ? 1 : 0);
        nmsEntity.f(tag);
    }

    public void applyEffect(Entity ent, PotionEffectType effectType, double period, int amplifier) {
        new PotionEffectWrap(effectType.getId(), amplifier, (int) (period * 20), true, false).addTo((LivingEntity) ent);
    }

    public double calculatePlayerDefense(Player player) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        double f = 1000000;
        Function<Double, Double> armor = f12 -> {
            int i = 25 - nmsPlayer.br();
            double f1 = f12 * (float) i;
            return -(f12 - (f1 / 25.0f));
        };
        float armorModifier = armor.apply(f).floatValue();
        f += armorModifier;
        Function<Double, Double> resistance = f13 -> {
            if (nmsPlayer.hasEffect(MobEffectList.RESISTANCE)) {
                int i = (nmsPlayer.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f1 = f13.floatValue() * (float) j;
                return -(f13 - (double) (f1 / 25.0F));
            } else {
                return -0.0;
            }
        };
        float resistanceModifier = resistance.apply(f).floatValue();
        f += resistanceModifier;
        Function<Double, Double> magic = f14 -> -(f14 - (double) applyMagicModifier(nmsPlayer, f14.floatValue()));
        float magicModifier = magic.apply(f).floatValue();
        f += magicModifier;

        return (f / 1000000);
    }

    public float applyMagicModifier(EntityPlayer nmsPlayer, float f) {
        if (f <= 0.0F) {
            return 0.0F;
        } else {
            int i = EnchantmentManager.a(nmsPlayer.getEquipment(), DamageSource.mobAttack(nmsPlayer));
            if (i > 20) {
                i = 20;
            }
            if (i > 0 && i <= 20) {
                int j = 25 - i;
                float f1 = f * (float) j;
                f = f1 / 25.0F;
            }
            return f;
        }
    }

    public void setClip(LivingEntity entity, boolean b) {
        net.minecraft.server.v1_8_R3.Entity ent = ((CraftLivingEntity) entity).getHandle();
        ent.noclip = b;
    }

    public LivingEntity getNearestPlayer(LivingEntity entity, int i) {
        LivingEntity nearest = null;
        double distance = i;
        for (LivingEntity player : getNearbyPlayers(entity, i)) {
            if (nearest == null) {
                nearest = player;
                distance = player.getLocation().distance(entity.getLocation());
            } else {
                if (player.getLocation().distance(entity.getLocation()) < distance) {
                    nearest = player;
                    distance = player.getLocation().distance(entity.getLocation());
                }
            }
        }
        return nearest;
    }

    public void teleportTo(LivingEntity entity, Location location) {
        entity.teleport(location);
    }

    public List<LivingEntity> getNearbyPlayers(LivingEntity entity, double size) {
        return entity.getNearbyEntities(size, size, size)
                .stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .filter(e -> !e.equals(entity))
                .filter(e -> !(e instanceof Player) || ((Player)e).getGameMode() == GameMode.SURVIVAL)
                .collect(Collectors.toList());
    }

    public List<LivingEntity> getNearbyPlayers(Location location, double size) {
        return location.getWorld().getNearbyEntities(location, size, size, size)
                .stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .filter(e -> !(e instanceof Player) || ((Player)e).getGameMode() == GameMode.SURVIVAL)
                .collect(Collectors.toList());
    }
}
