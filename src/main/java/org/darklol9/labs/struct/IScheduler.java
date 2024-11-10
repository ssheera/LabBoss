package org.darklol9.labs.struct;

public interface IScheduler {

    void spawn(BossState bossState);

    void death(BossState bossState);

    void checkBossStates();
}
