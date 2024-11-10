package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.darklol9.labs.entity.MConf;

public class CmdBossAutoSpawn extends MassiveCommand {

    public CmdBossAutoSpawn() {
        setVisibility(Visibility.SECRET);
        setDesc("Manage auto spawns");
        setAliases(MConf.get().cmdBossAutoSpawnAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossAutoSpawnPermission));
        this.addChild(new CmdBossAutoSpawnAdd());
        this.addChild(new CmdBossAutoSpawnRemove());
        this.addChild(new CmdBossAutoSpawnList());
    }

}
