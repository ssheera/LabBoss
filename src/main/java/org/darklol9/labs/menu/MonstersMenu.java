package org.darklol9.labs.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.entity.MMenu;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.utils.wrapper.MenuWrapper;

import java.util.List;

public class MonstersMenu implements InventoryHolder {

    private final Inventory inventory;

    public MonstersMenu() {
        int j = 0;
        List<Boss> bosses = MConf.get().bosses;
        MenuWrapper defaultMenu = MMenu.get().monsterMenu;
        inventory = Bukkit.createInventory(this, defaultMenu.getSize(), ChatColor.translateAlternateColorCodes('&', defaultMenu.getTitle()));

        int k = 0;
        for (String row : defaultMenu.getDesign()) {
            for (int i = 0; i < row.length(); i++) {
                String key = String.valueOf(row.charAt(i));
                if (defaultMenu.getItems().containsKey(key)) {
                    inventory.setItem(k + i, defaultMenu.getItems().get(key).build());
                } else {
                    inventory.setItem(k + i, bosses.get(j++).getDisplayItem().build());
                }
            }
            k += 9;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
