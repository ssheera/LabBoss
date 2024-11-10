package org.darklol9.labs.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Coll;

@EditorName("config")
public class MLootColl extends Coll<MLoot> {

    private static final MLootColl i = new MLootColl();

    public static MLootColl get() {
        return i;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MLoot.i = this.get(MassiveCore.INSTANCE, true);
    }
}
