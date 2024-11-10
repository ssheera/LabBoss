package org.darklol9.labs.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Material;
import org.darklol9.labs.struct.MenuAction;
import org.darklol9.labs.utils.wrapper.MenuItemWrapper;
import org.darklol9.labs.utils.wrapper.MenuWrapper;

@EditorName("menus")
public class MMenu extends Entity<MMenu> {

    protected static MMenu i;

    // -------------------------------------------- //

    public MenuWrapper bossMenu = MenuWrapper
            .builder()
            .title("Bosses")
            .size(27)
            .item("#", MenuItemWrapper
                    .builder()
                    .name("")
                    .data((byte) 7)
                    .type(Material.STAINED_GLASS_PANE)
                    .build())
            .item("A", MenuItemWrapper
                    .builder()
                    .type(Material.IRON_BARDING)
                    .glow(true)
                    .name("&b&lBoss World")
                    .lore("")
                    .lore("&bDescription")
                    .lore("&8| &fBoss eggs can only be spawned in the &nBoss World&r&f.")
                    .lore("&8| &fPVP is &nNOT&r&f enabled in this world but you")
                    .lore("&8| &fare able to kill other peoples bosses!")
                    .lore("")
                    .lore("&eClick to &nTeleport")
                    .command("warp boss {player}")
                    .action(MenuAction.NONE)
                    .build())
            .item("B", MenuItemWrapper
                    .builder()
                    .type(Material.BOOK)
                    .glow(true)
                    .name("&b&lInformation")
                    .lore("")
                    .lore("&bDescription")
                    .lore("&8| &fBosses arew a PvE based feature that")
                    .lore("&8| &fanybody can compete for, a random boss will spawn")
                    .lore("&8| &fevery &n4&r&f hours in the boss world making")
                    .lore("&8| &fthe feature accessible to all, alongside being")
                    .lore("&8| &fobtainable throughout the entire server to")
                    .lore("&8| &fbe forcefully spawned in by the player.")
                    .lore("")
                    .lore("&bBoss Types")
                    .lore("  &3➟ &fOlaf")
                    .lore("")
                    .lore("&4&lBEWARE &8» &cBosses can be &nEXTREMELY&r&c difficult!")
                    .action(MenuAction.NONE)
                    .build())
            .item("C", MenuItemWrapper
                    .builder()
                    .type(Material.DIAMOND_SWORD)
                    .glow(true)
                    .name("&b&lMonsters")
                    .lore("")
                    .lore("  &3➟ &fClick to view all &nMonsters")
                    .action(MenuAction.MONSTERS)
                    .build())
            .row("#########")
            .row("# A B C #")
            .row("#########")
            .build();

    public MenuWrapper monsterMenu = MenuWrapper
            .builder()
            .title("Monsters")
            .size(18)
            .item("#", MenuItemWrapper
                    .builder()
                    .name("")
                    .data((byte) 7)
                    .type(Material.STAINED_GLASS_PANE)
                    .build())
            .row("#### ####")
            .row("#### ####")
            .build();

    // -------------------------------------------- //

    public static MMenu get() {
        return i;
    }
}
