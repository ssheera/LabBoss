package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.darklol9.labs.entity.MAutoSpawns;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.AutoSpawn;

import java.util.List;

public class CmdBossAutoSpawnRemove extends MassiveCommand {

    public CmdBossAutoSpawnRemove() {
        setVisibility(Visibility.SECRET);
        setDesc("Remove auto spawn");
        setAliases(MConf.get().cmdBossAutoSpawnRemoveAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossAutoSpawnRemovePermission));
        addParameter(TypeInteger.get(), "index");
    }

    @Override
    public void perform() throws MassiveException {
        List<AutoSpawn> autoSpawns = MAutoSpawns.get().getAutoSpawns();
        int index = readArg();
        if (index < 0 || index >= autoSpawns.size()) {
            msg("<b>Index must be between 0 and %s", autoSpawns.size() - 1);
            return;
        }
        AutoSpawn autoSpawn = autoSpawns.get(index);
        autoSpawn.delete();
        msg("<i>Auto spawn removed successfully!");
    }
}
