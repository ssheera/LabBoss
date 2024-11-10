package org.darklol9.labs.task;

import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Material;
import org.darklol9.labs.entity.MSpawners;
import org.darklol9.labs.struct.BossSpawner;

import java.util.ArrayList;
import java.util.List;

public class TaskSpawners extends ModuloRepeatTask {

    private static final TaskSpawners i = new TaskSpawners();

    public TaskSpawners() {
        super(50);
    }

    public static TaskSpawners get() {
        return i;
    }

    @Override
    public void invoke(long l) {
        List<BossSpawner> remove = new ArrayList<>();
        List<BossSpawner> spawners = MSpawners.get().getSpawners();
        for (BossSpawner spawner : spawners) {
            if (spawner.getBlock().getType() == Material.AIR)
                remove.add(spawner);
            else {
                spawner.checkBossStates();
                spawner.tick();
            }
        }
        for (BossSpawner spawner : remove)
            MSpawners.get().removeSpawner(spawner);
    }
}
