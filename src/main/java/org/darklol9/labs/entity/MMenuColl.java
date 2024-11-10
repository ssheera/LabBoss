package org.darklol9.labs.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Coll;

@EditorName("config")
public class MMenuColl extends Coll<MMenu> {

    private static final MMenuColl i = new MMenuColl();

    public static MMenuColl get() {
        return i;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MMenu.i = this.get(MassiveCore.INSTANCE, true);
    }
}
