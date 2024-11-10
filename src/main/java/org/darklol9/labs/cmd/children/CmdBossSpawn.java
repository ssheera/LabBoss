package org.darklol9.labs.cmd.children;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.darklol9.labs.cmd.type.TypeBoss;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.Boss;

public class CmdBossSpawn extends MassiveCommand {

    public CmdBossSpawn() {
        setVisibility(Visibility.SECRET);
        setDesc("Spawn a boss at a given location");
        setAliases(MConf.get().cmdBossSpawnAliases);
        addRequirements(RequirementHasPerm.get(MConf.get().cmdBossSpawnPermission));
        addParameter(TypeBoss.get(), "type");
        addParameter(TypePlayer.get(), "player");
    }

    @Override
    public void perform() throws MassiveException {
        Boss boss = readArg();
        Player player = readArg();
        Location location = player.getLocation();
        boss.spawn(location);
        msg("<g>Spawned boss <h>%s <g>at <h>%s", boss.getId(), player.getName());
    }
}
