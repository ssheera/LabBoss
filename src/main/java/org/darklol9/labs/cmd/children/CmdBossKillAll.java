package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.task.TaskBossTick;

public class CmdBossKillAll extends MassiveCommand {

    public CmdBossKillAll() {
        setVisibility(Visibility.SECRET);
        setDesc("Kill all bosses");
        setAliases(MConf.get().cmdBossKillAllAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossKillAllPermission));
    }

    @Override
    public void perform() {
        int killed = TaskBossTick.get().kill();
        msg("<i>Killed <h>%d <i>bosses.", killed);
    }
}
