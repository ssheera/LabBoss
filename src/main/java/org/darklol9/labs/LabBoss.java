package org.darklol9.labs;

import com.massivecraft.massivecore.MassivePlugin;
import org.darklol9.labs.cmd.CmdBoss;
import org.darklol9.labs.engine.EngineBoss;
import org.darklol9.labs.engine.EngineEgg;
import org.darklol9.labs.engine.EngineMenu;
import org.darklol9.labs.engine.EngineSpawner;
import org.darklol9.labs.entity.*;
import org.darklol9.labs.task.TaskAutoSpawn;
import org.darklol9.labs.task.TaskBossTick;
import org.darklol9.labs.task.TaskSpawners;

public class LabBoss extends MassivePlugin {

    private static LabBoss i;

    public LabBoss() {
        i = this;
    }

    public static LabBoss get() {
        return i;
    }

    @Override
    public void onEnableInner() {
        activate(
                MConfColl.class,
                MMenuColl.class,
                MAutoSpawnsColl.class,
                MSpawnersColl.class,
                MLootColl.class,
                CmdBoss.class,
                EngineEgg.class,
                EngineMenu.class,
                EngineBoss.class,
                EngineSpawner.class,
                TaskBossTick.class,
                TaskSpawners.class,
                TaskAutoSpawn.class
        );
    }

    @Override
    public void onDisable() {
        TaskBossTick.get().kill();
    }

    @Override
    public boolean isVersionSynchronized() {
        return false;
    }

}