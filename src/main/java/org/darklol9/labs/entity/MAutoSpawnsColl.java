package org.darklol9.labs.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MAutoSpawnsColl extends Coll<MAutoSpawns> {

    private static final MAutoSpawnsColl i = new MAutoSpawnsColl();

    public static MAutoSpawnsColl get() {
        return i;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MAutoSpawns.i = this.get(MassiveCore.INSTANCE, true);
    }

}
