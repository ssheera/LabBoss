package org.darklol9.labs.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.struct.SkillType;
import org.darklol9.labs.utils.wrapper.*;

import java.util.List;

@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static MConf i;

    // -------------------------------------------- //
    // BAR
    // -------------------------------------------- //

    public int maxBars = 25;
    public String healthBarSymbol = "▌";
    public String healthBarColor = "&c";
    public String shieldBarSymbol = "▌";
    public String shieldBarColor = "&b";
    public String missingHealthBarSymbol = "▌";
    public String missingHealthBarColor = "&8";

    // -------------------------------------------- //
    // PERM
    // -------------------------------------------- //

    public String cmdBossPermission = "labs.bosses.base";
    public String cmdBossAutoSpawnPermission = "labs.bosses.autospawn";
    public String cmdBossAutoSpawnAddPermission = "labs.bosses.autospawn.add";
    public String cmdBossAutoSpawnRemovePermission = "labs.bosses.autospawn.remove";
    public String cmdBossAutoSpawnListPermission = "labs.bosses.autospawn.list";
    public String cmdBossSpawnPermission = "labs.bosses.admin.spawn";
    public String cmdBossTimePermission = "labs.bosses.time";
    public String cmdBossReloadPermission = "labs.bosses.admin.reload";
    public String cmdBossListPermission = "labs.bosses.list";
    public String cmdBossKillAllPermission = "labs.bosses.admin.killall";
    public String cmdBossEggPermission = "labs.bosses.admin.egg";
    public String cmdBossSpawnerPermission = "labs.bosses.admin.spawner";

    // -------------------------------------------- //
    // ALIASES
    // -------------------------------------------- //

    public List<String> cmdBossAliases = MUtil.list("boss");
    public List<String> cmdBossAutoSpawnAliases = MUtil.list("autospawn");
    public List<String> cmdBossAutoSpawnListAliases = MUtil.list("list");
    public List<String> cmdBossAutoSpawnAddAliases = MUtil.list("add");
    public List<String> cmdBossAutoSpawnRemoveAliases = MUtil.list("remove");
    public List<String> cmdBossTimeAliases = MUtil.list("time");
    public List<String> cmdBossSpawnAliases = MUtil.list("spawn");
    public List<String> cmdBossEggAliases = MUtil.list("egg");
    public List<String> cmdBossSpawnerAliases = MUtil.list("spawner");
    public List<String> cmdBossListAliases = MUtil.list("list");
    public List<String> cmdBossKillAllAliases = MUtil.list("killall");
    public List<String> cmdBossReloadAliases = MUtil.list("reload");

    // -------------------------------------------- //
    // BOSS
    // -------------------------------------------- //

    public long bossSkillRate = 1000;

    public List<MiniBoss> minions = MUtil.list(
            MiniBoss
                    .builder()
                    .id("deadvillager")
                    .displayName("&bDead Villager")
                    .entityType(EntityType.ZOMBIE)
                    .maxHealth(50)
                    .damageReduction(0.5)
                    .attackDamage(30)
                    .attackSpeed(1)
                    .helmet(ItemWrapper
                            .builder()
                            .type(Material.DIAMOND_HELMET)
                            .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL.getName(), 5)
                            .unbreakable(true)
                            .build())
                    .chestplate(ItemWrapper
                            .builder()
                            .type(Material.DIAMOND_CHESTPLATE)
                            .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL.getName(), 5)
                            .unbreakable(true)
                            .build())
                    .leggings(ItemWrapper
                            .builder()
                            .type(Material.DIAMOND_LEGGINGS)
                            .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL.getName(), 5)
                            .unbreakable(true)
                            .build())
                    .boots(ItemWrapper
                            .builder()
                            .type(Material.DIAMOND_BOOTS)
                            .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL.getName(), 5)
                            .unbreakable(true)
                            .build())
                    .mainHand(ItemWrapper
                            .builder()
                            .type(Material.DIAMOND_SWORD)
                            .enchantment(Enchantment.DAMAGE_ALL.getName(), 5)
                            .unbreakable(true)
                            .build())
                    .effects(MUtil.list(
                            PotionWrapper
                                    .builder()
                                    .type(PotionEffectType.SPEED.getName())
                                    .amplifier(5)
                                    .duration(-1)
                                    .build()
                    ))
                    .build()
    );

    public List<Boss> bosses = MUtil.list(
            Boss.builder()
                    .id("vampire")
                    .lowestPlacementToGiveRewards(-1)
                    .displayName("&c&lVampire")
                    .entityType(EntityType.SNOWMAN)
                    .displayItem(ItemWrapper.builder().type(Material.STONE).build())
                    .summonerItem(ItemWrapper.builder()
                                    .type(Material.MONSTER_EGG)
                                    .data((byte) 56)
                                    .build())
                    .maxHealth(500)
                    .healthRegen(5)
                    .damageReduction(0.25)
                    .skillUsageRadius(8)
                    .attackDamage(10)
                    .attackSpeed(40)
                    .rangedAttackRange(30)
                    .hasHealthBar(true)
                    .skills(SkillsWrapper.builder()
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.SHIELD)
                                    .cooldown(2500)
                                    .chance(0.05)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.BLINK)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.PULL)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("size", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.REJUVENATE)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("ratio", 0.25d)
                                    .option("duration", 5000d)
                                    .option("radius", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.DAMAGESTACK)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.TRANSFUSION)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(30)
                                    .option("size", 4d)
                                    .option("healRatio", 0.75d)
                                    .build())
                            .build())
                    .build(),
            Boss.builder()
                    .id("villagechief")
                    .lowestPlacementToGiveRewards(-1)
                    .displayName("&b&lVillage Chief")
                    .entityType(EntityType.IRON_GOLEM)
                    .displayItem(ItemWrapper.builder()
                            .type(Material.SKULL_ITEM)
                            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UxZmFjM2Q5NjM0NmU2MjJlODkwZjc2ZWMwMTVhNzA5YjY3MzQyMjI1N2IxNDQyMDYxYTNhYTMyNTk4MjQxMSJ9fX0=")
                            .data((byte) 3)
                            .name("&b&lVillage Chief")
                            .lore("&7&oVery Difficult Boss")
                            .lore("")
                            .lore("&7The Village Chief is the leader of")
                            .lore("&7the village. He is a very strong")
                            .lore("&7warrior and has a lot of health.")
                            .lore("&7He is out to get revenge on the")
                            .lore("&7players for killing his people.")
                            .lore("&7Can you stop him?")
                            .lore("")
                            .lore("&b&lDetails")
                            .lore("   &b➟ &bType: &7Iron Golem")
                            .lore("   &b➟ &bHealth: &72000HP")
                            .lore("   &b➟ &bRegen: &710/s")
                            .lore("   &b➟ &bEffects: &7Speed")
                            .lore("   &b➟ &bResistance: &740%")
                            .lore("   &b➟ &bSkills: &7All")
                            .build())
                    .summonerItem(ItemWrapper.builder()
                            .type(Material.MONSTER_EGG)
                            .data((byte) 56)
                            .name("&b&lBoss Summoner &7(Village Chief)")
                            .lore("&7&oVery Difficult Boss")
                            .lore("")
                            .lore("&7The Village Chief is the leader of")
                            .lore("&7the village. He is a very strong")
                            .lore("&7warrior and has a lot of health.")
                            .lore("&7He is out to get revenge on the")
                            .lore("&7players for killing his people.")
                            .lore("&7Can you stop him?")
                            .lore("")
                            .lore("&b&lDetails")
                            .lore("   &b➟ &bType: &7Iron Golem")
                            .lore("   &b➟ &bHealth: &72000HP")
                            .lore("   &b➟ &bRegen: &710/s")
                            .lore("   &b➟ &bEffects: &7Speed")
                            .lore("   &b➟ &bResistance: &740%")
                            .lore("   &b➟ &bSkills: &7All")
                            .build())
                    .maxHealth(2000)
                    .healthRegen(10)
                    .damageReduction(0.4)
                    .skillUsageRadius(5)
                    .attackDamage(15)
                    .attackSpeed(5)
                    .hasHealthBar(true)
                    .effects(MUtil.list(
                            PotionWrapper.builder()
                                    .type(PotionEffectType.SPEED.getName())
                                    .duration(-1)
                                    .amplifier(3)
                                    .build())
                    )
                    .skills(SkillsWrapper.builder()
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.BLEED)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(10)
                                    .option("period", 3d)
                                    .option("size", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.BLIND)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("period", 5d)
                                    .option("size", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.BLINK)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.IMMOLATE)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(10)
                                    .option("period", 3d)
                                    .option("size", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.LIGHTNING)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(10)
                                    .option("size", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.PULL)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("size", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.PUSH)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("size", 3d)
                                    .option("force", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.REJUVENATE)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .option("ratio", 0.25d)
                                    .option("duration", 5000d)
                                    .option("radius", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.SELF_DESTRUCT)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(50)
                                    .option("startRadius", 5d)
                                    .option("reducedRadius", 10d)
                                    .option("wait", 5d)
                                    .option("reducedDamageRate", 0.4d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.WEB)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(50)
                                    .option("size", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.DAMAGESTACK)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.HEARTBREAKER)
                                    .cooldown(3000)
                                    .chance(0.1)
                                    .damage(20)
                                    .option("launchDelay", 2d)
                                    .option("freezePeriod", 3d)
                                    .option("resetDelay", 10d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.MINIONS)
                                    .cooldown(5000)
                                    .chance(0.1)
                                    .option("minion", "deadvillager")
                                    .option("spawnAmount", 5d)
                                    .option("removeTime", 5d)
                                    .option("nearbyRadius", 5d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.TRANSFUSION)
                                    .cooldown(5000)
                                    .chance(0.1)
                                    .damage(30)
                                    .option("size", 4d)
                                    .option("healRatio", 0.75d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.DECIMATINGSMASH)
                                    .cooldown(5000)
                                    .chance(0.1)
                                    .damage(20)
                                    .option("size", 5d)
                                    .option("jumpDelay", 1d)
                                    .option("jumpForce", 1.5d)
                                    .option("freezePeriod", 5d)
                                    .option("knockForce", 3d)
                                    .build())
                            .skill(SkillWrapper
                                    .builder()
                                    .skillType(SkillType.HALLUCINATE)
                                    .cooldown(5000)
                                    .chance(0.1)
                                    .option("healthRatio", 0.25d)
                                    .option("resistanceRatio", 0.5d)
                                    .option("damageRatio", 0.7d)
                                    .option("spawnAmount", 1d)
                                    .option("removeTime", 20d)
                                    .option("nearbyRadius", 5d)
                                    .build())
                            .build())
                    .messages(MessagesWrapper.builder()
                            .spawn("")
                            .spawn("&b&lBosses &8➸ &b&lThe Village Chief &7has awoken!")
                            .spawn("")
                            .death("")
                            .death("&b&lBosses &8➸ &b&lThe Village Chief &7has been slain!")
                            .death("")
                            .death("&b&lSlayers: ")
                            .onDeathFormat("    &b{position} &8➸ &f{player} &8(&f{%}%&8)")
                            .build())
                    .drops(DropsWrapper
                            .builder()
                            .natural(false)
                            .loot("100;vc-rose")
                            .build())
                    .build(),
            Boss
                    .builder()
                    .id("deviouschicken")
                    .displayName("&cDevious Chicken")
                    .entityType(EntityType.CHICKEN)
                    .displayItem(ItemWrapper.builder()
                            .type(Material.SKULL_ITEM)
                            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU4ZTYxNzJlYzI4NjhiOGM1YjEzMmYxNDRmMDA5NzA2NjYzYTM0M2QzZWU1NDUwYmViY2FlYTg1NmYxYTUxMSJ9fX0=")
                            .data((byte) 3)
                            .name("&cDevious Chicken")
                            .lore("&7&oNovice Mob")
                            .lore("")
                            .lore("&7Devious Chicken is a chicken that is one")
                            .lore("&7of the most devious chickens in the world.")
                            .lore("")
                            .lore("&c&lDetails")
                            .lore("   &c➟ &cType: &7Chicken")
                            .lore("   &c➟ &cHealth: &7100HP")
                            .lore("   &c➟ &cRegen: &71/s")
                            .lore("   &c➟ &cEffects: &7Speed")
                            .lore("   &c➟ &cResistance: &710%")
                            .lore("   &c➟ &cSkills: &7Immolate")
                            .build())
                    .summonerItem(ItemWrapper.builder()
                            .type(Material.MONSTER_EGG)
                            .data((byte) 40)
                            .name("&cMob Summoner &7(Devious Chicken)")
                            .lore("&7&oNovice Mob")
                            .lore("")
                            .lore("&7Devious Chicken is a chicken that is one")
                            .lore("&7of the most devious chickens in the world.")
                            .lore("")
                            .lore("&c&lDetails")
                            .lore("   &c➟ &cType: &7Chicken")
                            .lore("   &c➟ &cHealth: &7100HP")
                            .lore("   &c➟ &cRegen: &71/s")
                            .lore("   &c➟ &cEffects: &7Speed")
                            .lore("   &c➟ &cResistance: &710%")
                            .lore("   &c➟ &cSkills: &7Immolate")
                            .build())
                    .maxHealth(100)
                    .healthRegen(1)
                    .damageReduction(0.1)
                    .skillUsageRadius(5)
                    .attackDamage(3)
                    .attackSpeed(10)
                    .skills(SkillsWrapper.builder()
                            .overallChance(1.0)
                            .skill(
                                    SkillWrapper.builder()
                                            .skillType(SkillType.IMMOLATE)
                                            .cooldown(5000)
                                            .chance(0.1)
                                            .damage(3)
                                            .build()
                            )
                            .build())
                    .build()
    );

    // -------------------------------------------- //
    // SPAWNING
    // -------------------------------------------- //

    public int maxAutoSpawnAmount = 3;
    public int minSpawnDelay = 20 * 5;
    public int maxSpawnDelay = 20 * 10;
    public int minSpawnNearbyRadius = 20;
    public int spawnerSpawnAreaRadius = 5;
    public int minSpawnAmount = 1;
    public int maxSpawnAmount = 3;
    public int maxNearbySameBoss = 10;

    // -------------------------------------------- //
    // SPAWNER BLOCK
    // -------------------------------------------- //

    public ItemWrapper spawnerBlock = ItemWrapper.builder()
            .type(Material.SKULL_ITEM)
            .data((byte) 3)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZkZThkNTgzZWViZjRhMzM2MWNlOTVjN2Q2NGNiNTJkMzU4NTcwNTlkNTE0NjJiOGE5MDkzYTQxYmZlMzljMCJ9fX0=")
            .name("&b&lBoss Spawner")
            .lore("&7Place this block to spawn &f{boss}")
            .build();

    public static MConf get() {
        return i;
    }
}
