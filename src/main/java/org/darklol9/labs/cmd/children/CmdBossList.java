package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.bukkit.entity.Player;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.menu.MonstersMenu;

public class CmdBossList extends MassiveCommand {

    public CmdBossList() {
        setDesc("View the boss list");
        setAliases(MConf.get().cmdBossListAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossListPermission));
    }

    @Override
    public void perform() {
        if (senderIsConsole) {
            msg("<b>This command can only be used by players");
        } else {
            MonstersMenu menu = new MonstersMenu();
            Player player = (Player) sender;
            player.openInventory(menu.getInventory());
        }
    }
}
