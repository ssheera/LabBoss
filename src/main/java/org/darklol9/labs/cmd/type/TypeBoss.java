package org.darklol9.labs.cmd.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import org.bukkit.command.CommandSender;
import org.darklol9.labs.entity.MConf;
import org.darklol9.labs.struct.Boss;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeBoss extends TypeAbstract<Boss> {

    private static final TypeBoss i = new TypeBoss();

    public TypeBoss() {
        super(Boss.class);
    }

    public static TypeBoss get() {
        return i;
    }

    @Override
    public Boss read(String s, CommandSender commandSender) throws MassiveException {
        Boss boss = MConf.get().bosses.stream().filter(b -> b.getId().equalsIgnoreCase(s)).findFirst().orElse(null);
        if (boss == null)
            throw new MassiveException().addMsg("<b>Unknown boss <h>%s<b>.", s);
        return boss;
    }

    @Override
    public Collection<String> getTabList(CommandSender commandSender, String s) {
        return MConf.get().bosses.stream().map(Boss::getId).collect(Collectors.toList());

    }
}
