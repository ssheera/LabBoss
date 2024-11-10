package org.darklol9.labs.entity;

import com.massivecraft.massivecore.store.Entity;
import org.darklol9.labs.struct.AutoSpawn;

import java.util.ArrayList;
import java.util.List;

public class MAutoSpawns extends Entity<MAutoSpawns> {

    protected static MAutoSpawns i;
    private List<AutoSpawn> autoSpawns = new ArrayList<>();

    public static MAutoSpawns get() {
        return i;
    }

    public List<AutoSpawn> getAutoSpawns() {
        return autoSpawns;
    }

    public void setAutoSpawns(List<AutoSpawn> autoSpawns) {
        this.autoSpawns = autoSpawns;
    }

    public void save(AutoSpawn autoSpawn) {
        if (!this.autoSpawns.contains(autoSpawn))
            this.autoSpawns.add(autoSpawn);
        changed();
    }

    public void delete(AutoSpawn autoSpawn) {
        this.autoSpawns.remove(autoSpawn);
        changed();
    }
}
