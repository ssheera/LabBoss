package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.darklol9.labs.entity.*;

public class CmdBossReload extends MassiveCommand {

    public CmdBossReload() {
        setVisibility(Visibility.SECRET);
        setDesc("Reload the config");
        setAliases(MConf.get().cmdBossReloadAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossReloadPermission));
    }

    @Override
    public void perform() {
        MConfColl.get().syncAll();
        MMenuColl.get().syncAll();
        MAutoSpawnsColl.get().syncAll();
        MSpawnersColl.get().syncAll();
        MLootColl.get().syncAll();
        msg("<i>Config reloaded!");
    }
}
