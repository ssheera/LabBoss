package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.TypePS;
import com.massivecraft.massivecore.command.type.primitive.TypeLong;
import com.massivecraft.massivecore.ps.PS;
import org.darklol9.labs.cmd.type.TypeBoss;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.AutoSpawn;
import org.darklol9.labs.struct.Boss;

public class CmdBossAutoSpawnAdd extends MassiveCommand {

    public CmdBossAutoSpawnAdd() {
        setVisibility(Visibility.SECRET);
        setDesc("Add auto spawn");
        setAliases(MConf.get().cmdBossAutoSpawnAddAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossAutoSpawnAddPermission));
        addParameter(TypeBoss.get(), "boss");
        addParameter(TypeLong.get(), "schedule");
        addParameter(TypePS.get(), "location", "here");
    }

    @Override
    public void perform() throws MassiveException {
        Boss boss = this.readArg();
        long schedule = this.readArg();
        PS location;
        if (senderIsConsole)
            location = this.readArg();
        else
            location = PS.valueOf(me.getLocation());
        AutoSpawn autoSpawn = new AutoSpawn(location, boss.getId(), schedule);
        autoSpawn.save();
        msg("<i>Auto spawn added successfully!");
    }
}
