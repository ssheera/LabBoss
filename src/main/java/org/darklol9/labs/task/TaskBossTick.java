package org.darklol9.labs.task;

import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.darklol9.labs.struct.BossState;

import java.util.ArrayList;
import java.util.List;

public class TaskBossTick extends ModuloRepeatTask {

    public static TaskBossTick i = new TaskBossTick();
    private final List<BossState> bossStates;

    public TaskBossTick() {
        super(100L);
        bossStates = new ArrayList<>();
    }

    public static TaskBossTick get() {
        return i;
    }

    @Override
    public void invoke(long l) {
        for (BossState bossState : bossStates) {
            bossState.tick(l);
        }
        bossStates.removeIf(bossState -> !bossState.isAlive());
    }

    public void addState(BossState bossState) {
        bossStates.add(bossState);
    }

    public BossState getState(Entity entity) {
        return bossStates.stream()
                .filter(bossState -> {
                    LivingEntity boss = bossState.getEntity();
                    if (boss == null)
                        return false;
                    World world = boss.getWorld();
                    if (world == null)
                        return false;
                    if (!world.equals(entity.getWorld()))
                        return false;
                    return boss.getEntityId() == entity.getEntityId();
                })
                .findFirst().orElse(null);
    }

    public int kill() {
        int count = bossStates.size();
        for (BossState bossState : bossStates) {
            bossState.kill();
        }
        bossStates.clear();
        return count;
    }

    public List<BossState> getStates() {
        return bossStates;
    }
}
