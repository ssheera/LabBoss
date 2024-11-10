package org.darklol9.labs.entity;

import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Location;
import org.darklol9.labs.struct.BossSpawner;

import java.util.ArrayList;
import java.util.List;

public class MSpawners extends Entity<MSpawners> {

    protected static MSpawners i;
    private List<BossSpawner> spawners = new ArrayList<>();

    public static MSpawners get() {
        return i;
    }

    public void addSpawner(BossSpawner spawner) {
        spawners.add(spawner);
        changed();
    }

    public void removeSpawner(BossSpawner spawner) {
        spawners.remove(spawner);
        changed();
    }

    public List<BossSpawner> getSpawners() {
        return spawners;
    }

    public void setSpawners(List<BossSpawner> spawners) {
        this.spawners = spawners;
    }

    public BossSpawner getSpawner(Location location) {
        for (BossSpawner spawner : spawners) {
            if (spawner.getLocation().equals(location))
                return spawner;
        }
        return null;
    }
}
