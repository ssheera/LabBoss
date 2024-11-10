package org.darklol9.labs.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.bukkit.entity.Player;
import org.darklol9.labs.cmd.children.*;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.menu.BossMenu;

public class CmdBoss extends MassiveCommand {

    public static CmdBoss i = new CmdBoss();

    public CmdBoss() {
        setDesc("Base boss command");
        setAliases(MConf.get().cmdBossAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossPermission));
        addChild(new CmdBossSpawn());
        addChild(new CmdBossTime());
        addChild(new CmdBossReload());
        addChild(new CmdBossKillAll());
        addChild(new CmdBossEgg());
        addChild(new CmdBossList());
        addChild(new CmdBossAutoSpawn());
        addChild(new CmdBossSpawner());
    }

    public static CmdBoss get() {
        return i;
    }

    @Override
    public void perform() {
        if (senderIsConsole) {
            msg("<b>This command can only be used by players");
        } else {
            BossMenu menu = new BossMenu();
            Player player = (Player) sender;
            player.openInventory(menu.getInventory());
        }
    }
}
