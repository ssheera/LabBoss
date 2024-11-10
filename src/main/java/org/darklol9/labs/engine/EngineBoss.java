package org.darklol9.labs.engine;

import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.darklol9.labs.skills.SpawningSpecialSkill;
import org.darklol9.labs.skills.active.WebSkill;
import org.darklol9.labs.struct.BossState;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.struct.SkillType;
import org.darklol9.labs.task.TaskBossTick;

import java.util.List;

public class EngineBoss extends Engine {

    public static EngineBoss i = new EngineBoss();

    public static EngineBoss get() {
        return i;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

//        if (!(damager instanceof Player))
//            return;

//        Player player = (Player) damager;

        Entity entity = event.getEntity();

        BossState bossState = BossState.get(entity);

        if (bossState == null)
            return;

        event.setDamage(bossState.onDamage(event.getFinalDamage()));

        if (damager instanceof Player)
            bossState.onHit((Player) damager, event.getFinalDamage());
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileShoot(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) shooter;
                BossState bossState = BossState.get(ent);
                if (bossState != null)
                    event.setDamage(bossState.getBoss().getAttackDamage());
                else {
                    List<BossState> states = TaskBossTick.get().getStates();
                    for (BossState state : states) {
                        List<SpawningSpecialSkill> minionsSkills = state.getAbstractSkill(SpawningSpecialSkill.class);
                        for (SpawningSpecialSkill sk : minionsSkills) {
                            MiniBoss minion = sk.get(ent);
                            if (minion != null) {
                                event.setDamage(minion.getAttackDamage());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.WEB) {
            List<BossState> states = TaskBossTick.get().getStates();
            for (BossState state : states) {
                List<WebSkill> webSkills = state.getAbstractSkill(SkillType.WEB);
                for (WebSkill sk : webSkills) {
                    if (sk.isWeb(block)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        BossState bossState = BossState.get(entity);

        if (bossState != null)
            bossState.onDeath(event);
        else {
            List<BossState> states = TaskBossTick.get().getStates();
            for (BossState state : states) {
                List<SpawningSpecialSkill> minionsSkills = state.getAbstractSkill(SpawningSpecialSkill.class);
                for (SpawningSpecialSkill sk : minionsSkills) {
                    MiniBoss minion = sk.get(entity);
                    if (minion != null) {
                        event.getDrops().clear();
                        return;
                    }
                }
            }
        }
    }

}
