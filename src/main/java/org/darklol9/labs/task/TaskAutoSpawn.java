package org.darklol9.labs.task;

import com.massivecraft.massivecore.ModuloRepeatTask;
import org.darklol9.labs.entity.MAutoSpawns;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.AutoSpawn;
import org.darklol9.labs.struct.Boss;

import java.util.List;

public class TaskAutoSpawn extends ModuloRepeatTask {

    private static final TaskAutoSpawn i = new TaskAutoSpawn();

    public TaskAutoSpawn() {
        super(1000);
    }

    public static TaskAutoSpawn get() {
        return i;
    }

    @Override
    public void invoke(long l) {
        List<AutoSpawn> autoSpawns = MAutoSpawns.get().getAutoSpawns();
        for (AutoSpawn autoSpawn : autoSpawns) {
            if (autoSpawn.getRemainingTime() <= 0) {
                Boss boss = MConf.get().bosses.stream().filter(b -> b.getId().equals(autoSpawn.getBossId())).findFirst().orElse(null);
                if (boss != null) {
                    autoSpawn.checkBossStates();
                    if (boss.spawn(autoSpawn, autoSpawn.getLocation()))
                        if (autoSpawn.getBossStates().size() > MConf.get().maxAutoSpawnAmount)
                            autoSpawn.killFirst();
                } else {
                    RuntimeException ex = new RuntimeException("Boss with id " + autoSpawn.getBossId() + " not found!");
                    ex.printStackTrace();
                }
                autoSpawn.reschedule();
            }
        }
    }
}
