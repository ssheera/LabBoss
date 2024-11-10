package org.darklol9.labs.struct;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.darklol9.labs.LabBoss;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.ItemWrapper;
import org.darklol9.labs.utils.wrapper.PotionWrapper;

import java.util.List;

@Getter
public class MiniBoss extends AbstractBoss {

    @Builder
    public MiniBoss(String id, String displayName, EntityType entityType, double maxHealth, double damageReduction, double attackDamage, double attackSpeed, double rangedAttackRange, ItemWrapper helmet, ItemWrapper chestplate, ItemWrapper leggings, ItemWrapper boots, ItemWrapper mainHand, List<PotionWrapper> effects) {
        super(id, displayName, entityType, maxHealth, damageReduction, attackDamage, attackSpeed, rangedAttackRange, helmet, chestplate, leggings, boots, mainHand, effects);
    }

    public LivingEntity spawn(Location location) {

        Chunk chunk = location.getChunk();

        chunk.load();

        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location.clone().add(0, 1, 0), entityType);

        entity.setMetadata("bossName" , new FixedMetadataValue(LabBoss.get(), id));

        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
        entity.setCustomNameVisible(true);

        entity.setRemoveWhenFarAway(false);
        entity.setCanPickupItems(false);

        if (helmet != null) entity.getEquipment().setHelmet(helmet.build());
        if (chestplate != null) entity.getEquipment().setChestplate(chestplate.build());
        if (leggings != null) entity.getEquipment().setLeggings(leggings.build());
        if (boots != null) entity.getEquipment().setBoots(boots.build());

        if (mainHand != null) entity.getEquipment().setItemInHand(mainHand.build());

        if (effects != null)
            for (PotionWrapper effect : effects)
                effect.toPotionEffectWrap().addTo(entity);

        if (entity instanceof Ageable) {
            Ageable ageable = (Ageable) entity;
            ageable.setAdult();
            ageable.setAgeLock(true);
        }

        Entity passenger = entity.getPassenger();
        if (passenger != null) {
            passenger.remove();
            entity.setPassenger(null);
        }

        MobHelper.setupEntity(entity, attackDamage, attackSpeed, rangedAttackRange);

        entity.setMaxHealth(maxHealth);
        entity.setHealth(maxHealth);

        return entity;
    }
}
