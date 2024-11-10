package org.darklol9.labs.utils.helper;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.darklol9.labs.entity.MConf;

@UtilityClass
public class BarHelper {

    public String getBossBar(double maxHealth, double health, double shield) {

        int maxBars = MConf.get().maxBars;
        int bars = (int) Math.ceil(health / maxHealth * maxBars);

        StringBuilder builder = new StringBuilder();
        builder.append(MConf.get().healthBarColor);
        for (int i = 0; i < bars; i++)
            builder.append(MConf.get().healthBarSymbol);
        builder.append(MConf.get().missingHealthBarColor);
        for (int i = 0; i < maxBars - bars; i++)
            builder.append(MConf.get().missingHealthBarSymbol);

        int sbars = (int) Math.ceil(shield / maxHealth * maxBars);
        builder.append(MConf.get().shieldBarColor);
        for (int i = 0; i < sbars; i++)
            builder.append(MConf.get().shieldBarSymbol);

        return ChatColor.translateAlternateColorCodes('&', builder.toString());

    }

}
