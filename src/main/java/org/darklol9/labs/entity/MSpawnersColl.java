package org.darklol9.labs.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MSpawnersColl extends Coll<MSpawners> {

    private static final MSpawnersColl i = new MSpawnersColl();

    public static MSpawnersColl get() {
        return i;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MSpawners.i = this.get(MassiveCore.INSTANCE, true);
    }

}
