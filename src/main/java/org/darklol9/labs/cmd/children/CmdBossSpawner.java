package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.darklol9.labs.cmd.type.TypeBoss;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.utils.wrapper.NbtWrapper;

import java.util.ArrayList;
import java.util.List;

public class CmdBossSpawner extends MassiveCommand {

    public CmdBossSpawner() {
        setVisibility(Visibility.SECRET);
        setDesc("Give yourself a boss spawner");
        setAliases(MConf.get().cmdBossSpawnerAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossSpawnerPermission));
        addParameter(TypePlayer.get(), "player");
        addParameter(TypeBoss.get(), "type");
        addParameter(TypeInteger.get(), "amount", "1");
    }

    @Override
    public void perform() throws MassiveException {

        Player player = readArg();
        Boss type = readArg();
        int amount = readArg(1);

        ItemStack item = MConf.get().spawnerBlock.build();
        item.setAmount(amount);

        List<String> newLore = new ArrayList<>();
        List<String> lore = item.getItemMeta().getLore();

        lore.stream().map(s -> s.replace("{boss}", type.getId())).forEach(newLore::add);

        ItemMeta meta = item.getItemMeta();
        meta.setLore(newLore);
        item.setItemMeta(meta);

        NbtWrapper nbt = new NbtWrapper(item);
        nbt.set("bossSpawnerType", type.getId());

        item = nbt.getItem();

        player.getInventory().addItem(item);

        msg("<i>Given <h>%d <i>%s boss spawners to <h>%s<i>.", amount, type.getId(), player.getName());

    }
}
