package org.darklol9.labs.nms.pathfinding;

import net.minecraft.server.v1_8_R3.*;
import org.darklol9.labs.struct.BossState;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PathfinderGoalGoBack extends PathfinderGoal implements PathfinderGoalAsync {
    private final EntityCreature a;
    private final BossState bossState;
    private final Vec3D b;

    /**
     * The pending navigation process.
     */
    private CompletableFuture<PathEntity> pendingNavigation;

    public PathfinderGoalGoBack(EntityCreature ent, BossState bossState, double x, double y, double z) {
        this.a = ent;
        this.b = new Vec3D(x, y, z);
        this.bossState = bossState;
        this.a(1);
    }

    public boolean a() {
        return bossState.isGoBack();
    }

    public boolean b() {
        return !this.a.getNavigation().m();
    }

    public void c() {
        this.a.getNavigation().a(b.a, b.b, b.c, 1.0D);
    }

    @Override
    public InitStatus checkInitStatus() {
        try{
            if(pendingNavigation == null)
                return InitStatus.FAILED;

            if(pendingNavigation.isDone())
                return pendingNavigation.get() == null ? InitStatus.FAILED : InitStatus.READY;
            else
                return InitStatus.INCOMPLETE;
        }catch(InterruptedException | ExecutionException exc) {
            throw new RuntimeException("Failed to get the result of a path find!");
        }
    }

    @Override
    public boolean completeInit() {
        try{
            //Something caused this to be reset or this method was called twice.
            if(pendingNavigation == null)
                return false;

            PathEntity entity = pendingNavigation.get();
            pendingNavigation = null;
            if(entity == null)
                throw new IllegalStateException("Path entity for completeInit is null!");

            this.a.getNavigation().a(entity, 1.0D);
            return true;
        }catch(InterruptedException | ExecutionException exc) {
            //Don't do anything here, the server may have been stopped.
        }
        return false;
    }

    @Override
    public boolean checkAndBegin() {
        NavigationAbstract navigationAbstract = this.a.getNewNavigation(a.getWorld());
        CompletableFuture<PathEntity> pathFuture = new CompletableFuture<>();
        PATHFIND_SERVICE.submit(() -> {
            PathEntity path = navigationAbstract.a(b.a, b.b, b.c);
            pathFuture.complete(path);
        });
        pendingNavigation = pathFuture;
        return true;
    }
}
