package org.darklol9.labs.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Coll;

@EditorName("config")
public class MConfColl extends Coll<MConf> {

    private static final MConfColl i = new MConfColl();

    public static MConfColl get() {
        return i;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MConf.i = this.get(MassiveCore.INSTANCE, true);
    }
}
