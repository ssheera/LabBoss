package org.darklol9.labs.struct;

import com.massivecraft.massivecore.mixin.MixinActionbar;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.entity.MLoot;
import org.darklol9.labs.events.BossDeathEvent;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.task.TaskBossTick;
import org.darklol9.labs.utils.helper.BarHelper;
import org.darklol9.labs.utils.helper.MobHelper;
import org.darklol9.labs.utils.helper.WeightedList;
import org.darklol9.labs.utils.wrapper.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class BossState {

    private final Boss boss;
    private Map<UUID, Double> damageMap;
    private final Map<UUID, Long> lastAttackers;
    private final Map<SkillWrapper, Long> skillCooldowns;
    private final Map<SkillWrapper, List<AbstractSkill>> skillsMap;
    @Setter
    private double shield;
    private long lastRegen;
    private boolean disableSkills;
    private boolean isDead;

    private double skillUsageRadius;

    @Setter
    private double boostedSkillChance;

    private long lastSkill;

    private final LivingEntity entity;
    private final World world;

    @Setter
    private double dps;

    @SneakyThrows
    public BossState(Boss boss, LivingEntity entity) {
        this.boss = boss;
        this.entity = entity;
        this.world = entity.getWorld();
        this.shield = 0d;
        this.damageMap = new LinkedHashMap<>();
        this.lastAttackers = new LinkedHashMap<>();
        this.skillCooldowns = new LinkedHashMap<>();
        this.skillsMap = new LinkedHashMap<>();
        SkillsWrapper skills = boss.getSkills();
        for (SkillWrapper skill : skills.getSkills()) {
            List<AbstractSkill> skillsList = skillsMap.getOrDefault(skill, new ArrayList<>());
            skillsList.add(skill.getSkillType().getSkill().newInstance());
            skillsMap.put(skill, skillsList);
        }
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public static BossState get(Entity entity) {
        return TaskBossTick.get().getState(entity);
    }

    public void setSkillUsageRadius(double skillUsageRadius) {
        this.skillUsageRadius = skillUsageRadius;
    }

    public boolean isAlive() {
        World world = getWorld();
        if (world == null) return false;
        LivingEntity entity = getEntity();
        return entity != null && entity.getHealth() > 0 && !entity.isDead() && !isDead;
    }

    public void toggleSkills() {
        disableSkills = !disableSkills;
    }

    public void tick(long time) {
        if (!isAlive()) return;
        LivingEntity entity = getEntity();
        tickSkills(time);
        lastAttackers.entrySet().removeIf(entry -> time - entry.getValue() >= 5000);
        if (boss.isHasHealthBar())
            lastAttackers.forEach((key, value) -> {
                Player player = Bukkit.getPlayer(key);
                if (player != null)
//                    MixinActionbar.get().sendActionbarMsg(player,
//                            BarHelper.getBossBar(boss.getMaxHealth(), entity.getHealth(), shield));
                    MixinActionbar.get().sendActionbarMsg(player, "&c" + (int) entity.getHealth() + " HP &7| &b" + (int) shield + " Shield | &a" + (int) dps + " DPS");
            });
        if (time - lastRegen >= 1000) {
            lastRegen = time;
            if (isAlive())
                entity.setHealth(Math.min(entity.getMaxHealth(),
                        entity.getHealth() + boss.getHealthRegen()));
        }
    }

    private void tickSkills(long time) {

        if (!isAlive()) return;

        LivingEntity entity = getEntity();

        skillCooldowns.entrySet().removeIf(entry -> time - entry.getValue() >= entry.getKey().getCooldown());
        SkillsWrapper skills = boss.getSkills();

        skillsMap.forEach((skill, sks) -> {
            for (AbstractSkill sk : sks)
                sk.tick(time, this, skill);
        });

        if (time - lastSkill >= MConf.get().bossSkillRate) {
            skillsMap.forEach((skill, sks) -> {
                for (AbstractSkill sk : sks) {
                    boolean nearby = !MobHelper.getNearbyPlayers(entity, skillUsageRadius).isEmpty();
                    boolean chance = Math.random() <= (skills.getOverallChance() * (1 + boostedSkillChance)) * skill.getChance();
                    if (!sk.isActive() || (nearby && chance && !disableSkills)) {
                        if (sk.isActive() && skillCooldowns.containsKey(skill))
                            continue;
                        if (sk.isActive())
                            skillCooldowns.put(skill, time + skill.getCooldown());
                        sk.execute(this, skill);
                    }
                }
            });
            lastSkill = time;
        }
    }

    public void kill() {
        LivingEntity entity = getEntity();
        if (entity != null) entity.remove();
        isDead = true;
        skillsMap.forEach((skill, sks) -> {
            for (AbstractSkill sk : sks)
                sk.onDeath(this, skill);
        });
        skillsMap.clear();
        skillCooldowns.clear();
    }

    public void onHit(Player player, double damage) {
        lastAttackers.put(player.getUniqueId(), System.currentTimeMillis());
        damageMap.put(player.getUniqueId(),
                damageMap.getOrDefault(player.getUniqueId(), 0d) + damage);
    }

    public double onDamage(double damage) {
        SkillsWrapper skills = boss.getSkills();
        for (SkillWrapper skill : skills.getSkills()) {
            List<AbstractSkill> sks = skillsMap.get(skill);
            for (AbstractSkill sk : sks)
                damage = sk.onDamage(this, skill, damage);
        }
        damage *= (1 - boss.getDamageReduction());
        if (shield > 0) {
            double shieldDamage = Math.min(shield, damage);
            shield -= shieldDamage;
            damage -= shieldDamage;
        }
        return damage;
    }

    public void onSpawn() {
        MessagesWrapper messages = boss.getMessages();
        CommandsWrapper commands = boss.getCommands();
        for (String message : messages.getOnSpawn())
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        commands.getOnSpawn().forEach(command ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    public void onDeath(EntityDeathEvent event) {

        damageMap = damageMap.entrySet().stream().sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        BossDeathEvent bossDeathEvent = new BossDeathEvent(this, damageMap);
        bossDeathEvent.run();

        IScheduler scheduler = boss.getScheduler();
        if (scheduler != null)
            scheduler.death(this);

        isDead = true;

        skillsMap.forEach((skill, sks) -> {
            for (AbstractSkill sk : sks)
                sk.onDeath(this, skill);
        });

        CommandsWrapper commands = boss.getCommands();

        if (commands != null) commands.getOnDeath().forEach(command ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        DropsWrapper drops = boss.getDrops();
        if (drops != null && !drops.isNatural()) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
        List<String> deathMessage = new ArrayList<>();
        MessagesWrapper messages = boss.getMessages();
        if (messages != null)
            deathMessage.addAll(messages.getOnDeath());

        double totalDamage = damageMap.values().stream().mapToDouble(Double::doubleValue).sum();
        AtomicInteger position = new AtomicInteger(1);

        if (boss.getLowestPlacementToGiveRewards() == 0) boss.setLowestPlacementToGiveRewards(-1);

        damageMap.forEach((key, value) -> {
            if (boss.getLowestPlacementToGiveRewards() != -1 &&
                    boss.getLowestPlacementToGiveRewards() < position.get()) return;
            OfflinePlayer player = Bukkit.getOfflinePlayer(key);
            if (player != null) {
                double damage = value;
                double percent = damage / totalDamage * 100;
                if (messages != null) {
                    String message = messages.getOnDeathFormat();
                    if (message != null) {
                        message = message.replace("{player}", player.getName());
                        message = message.replace("{%}", String.format("%.2f", percent));
                        message = message.replace("{position}", String.valueOf(position.get()));
                        deathMessage.add(message);
                    }
                }
                if (drops != null) {
                    for (String s : drops.getLoot()) {
                        String[] split = s.split(";");
                        int chance = Integer.parseInt(split[0]);
                        if (Math.random() * 100 > chance) continue;
                        String lootId = split[1];
                        LootWrapper lw = MLoot.get().getLoot(lootId);
                        if (lw != null) {
                            WeightedList<String> loot = new WeightedList<>();
                            for (String command : lw.getCommands()) {
                                String[] cmdSplit = command.split(";");
                                int cmdChance = Integer.parseInt(cmdSplit[0]);
                                String cmd = cmdSplit[1].replace("{player}", player.getName());
                                loot.add(cmd, cmdChance);
                            }
                            if (loot.isEmpty()) continue;
                            loot.shuffle();
                            String cmd = loot.getRandom();
                            if (cmd == null) continue;
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        }
                    }
                }
                position.getAndIncrement();
            }
        });

        for (String s : deathMessage)
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));

        skillsMap.clear();
        skillCooldowns.clear();
    }

    public <T> List<T> getAbstractSkill(SkillType skillType) {
        List<T> skills = new ArrayList<>();
        skillsMap.forEach((skill, sks) -> {
            if (skill.getSkillType() == skillType)
                skills.addAll((Collection<? extends T>) sks);
        });
        return skills;
    }

    public <T> List<T> getAbstractSkill(Class<?> skillClass) {
        List<T> skills = new ArrayList<>();
        skillsMap.forEach((skill, sks) -> {
            if (skill.getSkillType().getSkill().isAssignableFrom(skillClass))
                skills.addAll((Collection<? extends T>) sks);
        });
        return skills;
    }

    public boolean isGoBack() {
        LivingEntity entity = getEntity();
        return lastAttackers.isEmpty() && !isDead && MobHelper.getNearbyPlayers(entity, 35).isEmpty();
    }

    public void heal(double v) {
        LivingEntity entity = getEntity();
        entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + v));
    }
}
