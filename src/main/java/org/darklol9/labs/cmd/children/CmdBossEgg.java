package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.darklol9.labs.cmd.type.TypeBoss;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.utils.wrapper.NbtWrapper;

public class CmdBossEgg extends MassiveCommand {

    public CmdBossEgg() {
        setVisibility(Visibility.SECRET);
        setDesc("Give yourself a boss egg");
        setAliases(MConf.get().cmdBossEggAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossEggPermission));
        addParameter(TypePlayer.get(), "player");
        addParameter(TypeBoss.get(), "type");
        addParameter(TypeInteger.get(), "amount", "1");
    }

    @Override
    public void perform() throws MassiveException {

        Player player = readArg();
        Boss type = readArg();
        int amount = readArg(1);

        ItemStack item = type.getSummonerItem().build();
        item.setAmount(amount);

        NbtWrapper nbt = new NbtWrapper(item);
        nbt.set("bossEggType", type.getId());

        item = nbt.getItem();

        player.getInventory().addItem(item);

        msg("<i>Given <h>%d <i>%s boss eggs to <h>%s<i>.", amount, type.getId(), player.getName());

    }
}
