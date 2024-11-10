package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import org.darklol9.labs.entity.MAutoSpawns;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.AutoSpawn;

import java.util.List;

public class CmdBossAutoSpawnList extends MassiveCommand {

    public CmdBossAutoSpawnList() {
        setVisibility(Visibility.SECRET);
        setDesc("View auto spawns");
        setAliases(MConf.get().cmdBossAutoSpawnListAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossAutoSpawnListPermission));
        addParameter(Parameter.getPage());
    }

    @Override
    public void perform() throws MassiveException {
        int page = this.readArg();
        List<AutoSpawn> autoSpawns = MAutoSpawns.get().getAutoSpawns();
        Pager<AutoSpawn> pager = new Pager<>(this, "AutoSpawns", page, (Stringifier<AutoSpawn>) (autoSpawn, i) ->
                String.format("%s %s", i, autoSpawn.toString()));
        pager.setItems(autoSpawns);
        pager.message();
    }
}
