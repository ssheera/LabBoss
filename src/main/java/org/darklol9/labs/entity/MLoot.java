package org.darklol9.labs.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.darklol9.labs.utils.wrapper.LootWrapper;

import java.util.List;

@EditorName("config")
public class MLoot extends Entity<MLoot> {

    protected static MLoot i;

    public List<LootWrapper> loot = MUtil.list(
            LootWrapper
                    .builder()
                    .id("vc-rose")
                    .command("50;say {player} defeated the village chief")
                    .build()
    );

    public static MLoot get() {
        return i;
    }

    public LootWrapper getLoot(String lootId) {
        for (LootWrapper lootWrapper : loot) {
            if (lootWrapper.getId().equalsIgnoreCase(lootId)) {
                return lootWrapper;
            }
        }
        return null;
    }
}
