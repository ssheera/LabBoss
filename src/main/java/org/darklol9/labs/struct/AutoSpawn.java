package org.darklol9.labs.struct;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import lombok.Getter;
import org.bukkit.Location;
import org.darklol9.labs.entity.MAutoSpawns;
import org.darklol9.labs.utils.helper.TimeHelper;

import java.util.LinkedHashSet;

@Getter
public class AutoSpawn implements IScheduler {

    private final PS location;
    private final long schedule;
    private final String bossId;
    private long next;
    private transient LinkedHashSet<BossState> bossStates;

    public AutoSpawn(PS location, String bossId, long schedule) {
        this.location = location;
        this.bossId = bossId;
        this.schedule = schedule;
        this.next = System.currentTimeMillis() + schedule;
        this.bossStates = new LinkedHashSet<>();
    }

    public long getRemainingTime() {
        return this.next - System.currentTimeMillis();
    }

    public Location getLocation() {
        return this.location.asBukkitLocation(true);
    }

    public void save() {
        MAutoSpawns.get().save(this);
    }

    public void delete() {
        MAutoSpawns.get().delete(this);
    }

    public void reschedule() {
        this.next = System.currentTimeMillis() + this.schedule;
        save();
    }

    @Override
    public void spawn(BossState bossState) {
        if (bossStates == null) bossStates = new LinkedHashSet<>();
        bossStates.add(bossState);
    }

    @Override
    public void death(BossState bossState) {
        if (bossStates == null) bossStates = new LinkedHashSet<>();
        bossStates.remove(bossState);
    }

    @Override
    public void checkBossStates() {
        if (bossStates == null) bossStates = new LinkedHashSet<>();
        bossStates.removeIf(bossState -> !bossState.isAlive());
    }

    public void killFirst() {
        if (bossStates.size() > 0) {
            BossState bossState = bossStates.iterator().next();
            bossState.kill();
            bossStates.remove(bossState);
        }
    }

    @Override
    public String toString() {
        Location loc = getLocation();
        return Txt.parse("&e%s &7(%s, %s, %s, %s) &a%s/%s", bossId, loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                TimeHelper.getTimeString(getRemainingTime()), TimeHelper.getTimeString(schedule));
    }
}
