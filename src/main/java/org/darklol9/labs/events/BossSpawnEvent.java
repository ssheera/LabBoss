package org.darklol9.labs.events;

import com.massivecraft.massivecore.event.EventMassiveCore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.darklol9.labs.struct.Boss;

@Getter
@AllArgsConstructor
public class BossSpawnEvent extends EventMassiveCore {

    private static final HandlerList handlers = new HandlerList();
    private final Boss boss;
    private final Location location;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
