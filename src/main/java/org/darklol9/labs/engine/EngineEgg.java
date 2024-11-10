package org.darklol9.labs.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.utils.wrapper.NbtWrapper;

public class EngineEgg extends Engine {

    public static EngineEgg i = new EngineEgg();

    public static EngineEgg get() {
        return i;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();

        ItemStack item = player.getItemInHand();

        if (item == null)
            return;

        if (item.getType() == Material.AIR)
            return;

        NbtWrapper nbt = new NbtWrapper(item);
        if (nbt.hasKey("bossEggType")) {
            event.setCancelled(true);
            Boss boss = MConf.get().bosses.stream().filter(b -> b.getId().equals(nbt.getString("bossEggType"))).findFirst().orElse(null);
            if (boss == null)
                return;
            if (!boss.spawn(block.getLocation())) {
                ParticleEffect.SMOKE_NORMAL.display(0.5f, 0.5f, 0.5f, 0.1f, 5, block.getLocation(), 100);
                return;
            }
            if (player.getGameMode() != GameMode.CREATIVE)
                item.setAmount(item.getAmount() - 1);
            if (item.getAmount() == 0)
                player.setItemInHand(null);
            player.updateInventory();
        }

    }

}
