package org.darklol9.labs.struct;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.darklol9.labs.LabBoss;
import org.darklol9.labs.events.BossSpawnEvent;
import org.darklol9.labs.task.TaskBossTick;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.wrapper.*;

import java.util.List;

@Getter
public class Boss extends AbstractBoss {

    private ItemWrapper displayItem;

    private ItemWrapper summonerItem;

    private double healthRegen;

    private double skillUsageRadius;

    private boolean hasHealthBar;

    private SkillsWrapper skills;

    private DropsWrapper drops;

    private MessagesWrapper messages;

    private CommandsWrapper commands;

    @Setter
    private int lowestPlacementToGiveRewards;

    private transient IScheduler scheduler;

    private transient LivingEntity entity;

    @Builder
    public Boss(String id, String displayName, EntityType entityType, double maxHealth, double damageReduction, double attackDamage, double attackSpeed, double rangedAttackRange, ItemWrapper helmet, ItemWrapper chestplate, ItemWrapper leggings, ItemWrapper boots, ItemWrapper mainHand, List<PotionWrapper> effects, ItemWrapper displayItem, ItemWrapper summonerItem, double healthRegen, double skillUsageRadius, boolean hasHealthBar, SkillsWrapper skills, DropsWrapper drops, MessagesWrapper messages, CommandsWrapper commands, int lowestPlacementToGiveRewards, IScheduler scheduler, LivingEntity entity) {
        super(id, displayName, entityType, maxHealth, damageReduction, attackDamage, attackSpeed, rangedAttackRange, helmet, chestplate, leggings, boots, mainHand, effects);
        this.displayItem = displayItem;
        this.summonerItem = summonerItem;
        this.healthRegen = healthRegen;
        this.skillUsageRadius = skillUsageRadius;
        this.hasHealthBar = hasHealthBar;
        this.skills = skills;
        this.drops = drops;
        this.messages = messages;
        this.commands = commands;
        this.lowestPlacementToGiveRewards = lowestPlacementToGiveRewards;
        this.scheduler = scheduler;
        this.entity = entity;
    }

    public boolean spawn(IScheduler scheduler, Location location) {
        this.scheduler = scheduler;
        return spawn(location);
    }

    public boolean spawn(Location location) {

        BossSpawnEvent event = new BossSpawnEvent(this, location);
        event.run();

        if (event.isCancelled()) return false;

        Chunk chunk = location.getChunk();

        chunk.load();

        entity = (LivingEntity) location.getWorld().spawnEntity(location.clone().add(0, 1, 0), entityType);

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

        if (skills == null) skills = SkillsWrapper.builder().build();

        if (drops == null) drops = DropsWrapper.builder().build();

        if (messages == null) messages = MessagesWrapper.builder().build();

        if (commands == null) commands = CommandsWrapper.builder().build();

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

        BossState bossState = new BossState(this, entity);
        TaskBossTick.get().addState(bossState);

//        TODO FIX LAG
//        MobHelper.setSpawnPathfinder(bossState);

        bossState.setSkillUsageRadius(skillUsageRadius);

        bossState.onSpawn();

        if (scheduler != null) scheduler.spawn(bossState);

        return true;
    }
}
