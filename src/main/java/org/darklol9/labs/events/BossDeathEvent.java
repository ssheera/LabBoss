package org.darklol9.labs.events;

import com.massivecraft.massivecore.event.EventMassiveCore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.darklol9.labs.struct.BossState;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BossDeathEvent extends EventMassiveCore {

    private static final HandlerList handlers = new HandlerList();
    private final BossState boss;
    private final Map<UUID, Double> damage;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getPlacement(UUID uuid) {
        int i = 0;
        for (UUID key : damage.keySet())
            if (key.equals(uuid))
                return i;
        return -1;
    }
}
