package org.darklol9.labs.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.entity.MSpawners;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.struct.BossSpawner;
import org.darklol9.labs.utils.wrapper.NbtWrapper;

public class EngineSpawner extends Engine {

    public static EngineSpawner i = new EngineSpawner();

    public static EngineSpawner get() {
        return i;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (item == null)
            return;

        if (item.getType() == Material.AIR)
            return;

        NbtWrapper nbt = new NbtWrapper(item);
        if (nbt.hasKey("bossSpawnerType")) {
            String type = nbt.getString("bossSpawnerType");
            Boss boss = MConf.get().bosses.stream().filter(b -> b.getId().equals(nbt.getString("bossSpawnerType"))).findFirst().orElse(null);
            if (boss == null)
                return;
            BossSpawner spawner = new BossSpawner(boss, PS.valueOf(event.getBlock().getLocation()));
            MSpawners.get().addSpawner(spawner);
            player.sendMessage(Txt.parse("<g>Spawner <h>%s <g>has been created.", type));
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        Location location = block.getLocation();

        BossSpawner spawner = MSpawners.get().getSpawner(location);

        if (spawner == null)
            return;

        MSpawners.get().removeSpawner(spawner);

        player.sendMessage(Txt.parse("<g>Spawner <h>%s <g>has been removed.", spawner.getBossId()));

    }

}
