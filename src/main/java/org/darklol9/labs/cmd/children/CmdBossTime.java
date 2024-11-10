package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import org.darklol9.labs.cmd.type.TypeBoss;
import org.darklol9.labs.entity.MAutoSpawns;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.AutoSpawn;
import org.darklol9.labs.struct.Boss;
import org.darklol9.labs.utils.helper.TimeHelper;

import java.util.List;

public class CmdBossTime extends MassiveCommand {

    public CmdBossTime() {
        setDesc("View boss schedule");
        setAliases(MConf.get().cmdBossTimeAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossTimePermission));
        addParameter(TypeBoss.get(), "boss");
    }

    @Override
    public void perform() throws MassiveException {
        Boss boss = readArg();
        long shortestTime = Long.MAX_VALUE;
        List<AutoSpawn> autoSpawns = MAutoSpawns.get().getAutoSpawns();
        for (AutoSpawn autoSpawn : autoSpawns) {
            if (autoSpawn.getBossId().equals(boss.getId())) {
                shortestTime = Math.min(shortestTime, autoSpawn.getRemainingTime());
            }
        }
        if (shortestTime == Long.MAX_VALUE) {
            msg("<i>There is no schedule for <h>%s<i>.", boss.getId());
        } else {
            msg("<b>Next spawn of <h>%s<b> in <h>%s<b>.", boss.getId(), TimeHelper.getTimeString(shortestTime));
        }
    }
}
