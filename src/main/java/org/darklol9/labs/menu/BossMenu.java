package org.darklol9.labs.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.darklol9.labs.entity.MMenu;
import org.darklol9.labs.utils.wrapper.MenuItemWrapper;
import org.darklol9.labs.utils.wrapper.MenuWrapper;

import java.util.HashMap;
import java.util.Map;

public class BossMenu implements InventoryHolder {

    private final Inventory inventory;

    private final Map<Integer, MenuItemWrapper> items;

    public BossMenu() {
        items = new HashMap<>();
        MenuWrapper defaultMenu = MMenu.get().bossMenu;
        inventory = Bukkit.createInventory(this, defaultMenu.getSize(),
                ChatColor.translateAlternateColorCodes('&', defaultMenu.getTitle()));
        int j = 0;
        for (String row : defaultMenu.getDesign()) {
            for (int i = 0; i < row.length(); i++) {
                String key = String.valueOf(row.charAt(i));
                if (defaultMenu.getItems().containsKey(key)) {
                    inventory.setItem(j + i, defaultMenu.getItems().get(key).build());
                    items.put(j + i, defaultMenu.getItems().get(key));
                }
            }
            j += 9;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public MenuItemWrapper get(int slot) {
        return items.get(slot);
    }
}
