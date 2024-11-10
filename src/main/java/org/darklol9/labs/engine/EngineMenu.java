package org.darklol9.labs.engine;

import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.darklol9.labs.menu.BossMenu;
import org.darklol9.labs.menu.MonstersMenu;
import org.darklol9.labs.struct.MenuAction;
import org.darklol9.labs.utils.wrapper.MenuItemWrapper;

public class EngineMenu extends Engine {

    public static EngineMenu i = new EngineMenu();

    public static EngineMenu get() {
        return i;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof BossMenu ||
                holder instanceof MonstersMenu) {

            if (holder instanceof BossMenu) {
                BossMenu bossMenu = (BossMenu) holder;
                MenuItemWrapper item = bossMenu.get(event.getSlot());
                if (item != null) {
                    for (String command : item.getCommands())
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", event.getWhoClicked().getName()));
                    MenuAction action = item.getAction();
                    if (action == MenuAction.MONSTERS) {
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(new MonstersMenu().getInventory());
                    }
                }
            }

            event.setResult(InventoryClickEvent.Result.DENY);
            event.setCancelled(true);
        }

    }

}
