package org.darklol9.labs.nms.pathfinding;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.darklol9.labs.utils.helper.MobHelper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class PathfinderGoalPassiveMeleeAttack extends PathfinderGoal implements PathfinderGoalAsync {
    protected EntityCreature b;
    World a;
    int c;
    double d;
    boolean e;
    PathEntity f;
    Class<? extends Entity> g;
    private int h;
    private double i;
    private double j;
    private double k;

    private int speed;
    private double attackDamage;

    /**
     * The pending navigation process.
     */
    private CompletableFuture<PathEntity> pendingNavigation;

    public PathfinderGoalPassiveMeleeAttack(EntityCreature var1, Class<? extends Entity> var2, double var3, boolean var5, double speed, double attackDamage) {
        this(var1, var3, var5, speed, attackDamage);
        this.g = var2;
    }

    public PathfinderGoalPassiveMeleeAttack(EntityCreature var1, double var2, boolean var4, double speed, double attackDamage) {
        this.b = var1;
        this.a = var1.world;
        this.d = var2;
        this.e = var4;
        this.speed = (int) speed;
        this.attackDamage = attackDamage;
        this.a(3);
    }

    public boolean a() {
        EntityLiving var1 = this.b.getGoalTarget();
        if (var1 == null) {
            return false;
        } else if (!var1.isAlive()) {
            return false;
        } else if (this.g != null && !this.g.isAssignableFrom(var1.getClass())) {
            return false;
        } else {
            this.f = this.b.getNavigation().a(var1);
            return this.f != null;
        }
    }

    public boolean b() {
        EntityLiving var1 = this.b.getGoalTarget();
        if (var1 == null) {
            return false;
        } else if (!var1.isAlive()) {
            return false;
        } else if (!this.e) {
            return !this.b.getNavigation().m();
        } else {
            return this.b.e(new BlockPosition(var1));
        }
    }

    public void c() {
        this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void d() {
        this.b.getNavigation().n();
    }

    public void e() {
        EntityLiving var1 = this.b.getGoalTarget();
        this.b.getControllerLook().a(var1, 30.0F, 30.0F);
        double var2 = this.b.e(var1.locX, var1.getBoundingBox().b, var1.locZ);
        double var4 = this.a(var1);
        --this.h;
        if ((this.e || this.b.getEntitySenses().a(var1)) && this.h <= 0 && (this.i == 0.0 && this.j == 0.0 && this.k == 0.0 || var1.e(this.i, this.j, this.k) >= 1.0 || this.b.bc().nextFloat() < 0.05F)) {
            this.i = var1.locX;
            this.j = var1.getBoundingBox().b;
            this.k = var1.locZ;
            this.h = 4 + this.b.bc().nextInt(7);
            if (var2 > 1024.0) {
                this.h += 10;
            } else if (var2 > 256.0) {
                this.h += 5;
            }

            if (!this.b.getNavigation().a(var1, this.d)) {
                this.h += 15;
            }
        }

        this.c = Math.max(this.c - 1, 0);
        if (var2 <= var4 && this.c == 0) {
            this.c = speed;
            if (this.b.bA() != null) {
                this.b.bw();
            }
            this.b.r(var1);
//            r(var1);
        }

    }

    public boolean r(Entity entity) {
        float f = (float) attackDamage;
        int i = 0;
        if (entity instanceof EntityLiving) {
            f += EnchantmentManager.a(b.bA(), ((EntityLiving) entity).getMonsterType());
            i += EnchantmentManager.a(b);
        }

        boolean flag = entity.damageEntity(DamageSource.mobAttack(b), f);
        if (flag) {
            if (i > 0) {
                entity.g(-MathHelper.sin(b.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F, 0.1, MathHelper.cos(b.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F);
                b.motX *= 0.6;
                b.motZ *= 0.6;
            }

            int j = EnchantmentManager.getFireAspectEnchantmentLevel(b);
            if (j > 0) {
                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(b.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                Bukkit.getPluginManager().callEvent(combustEvent);
                if (!combustEvent.isCancelled()) {
                    entity.setOnFire(combustEvent.getDuration());
                }
            }

            a(b, entity);
        }

        return flag;
    }

    protected void a(EntityLiving entityliving, Entity entity) {
        if (entity instanceof EntityLiving) {
            EnchantmentManager.a((EntityLiving) entity, entityliving);
        }

        EnchantmentManager.b(entityliving, entity);
    }

    protected double a(EntityLiving var1) {
        return this.b.width * 2.0F * this.b.width * 2.0F + var1.width;
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

            EntityLiving entityliving = this.b.getGoalTarget();

            PathEntity entity = pendingNavigation.get();
            pendingNavigation = null;
            if(entity == null)
                throw new IllegalStateException("Path entity for completeInit is null!");

            //Do the checks again as the goal target may have been changed.
            if (entityliving == null) {
                return false;
            } else if (!entityliving.isAlive()) {
                return false;
            } else if (this.g != null && !this.g.isAssignableFrom(entityliving.getClass())) {
                return false;
            }

            this.f = entity;
            this.c();
            return true;
        }catch(InterruptedException | ExecutionException exc) {
            //Don't do anything here, the server may have been stopped.
        }
        return false;
    }

    @Override
    public boolean checkAndBegin() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (this.g != null && !this.g.isAssignableFrom(entityliving.getClass())) {
            return false;
        } else {
            //Directly call the pathfinder for the path entity, rather than using the entity's navigation.
            NavigationAbstract navigationAbstract = this.b.getNewNavigation(this.a);
            CompletableFuture<PathEntity> pathFuture = new CompletableFuture<>();
            PATHFIND_SERVICE.submit(() -> {
                PathEntity path = navigationAbstract.a(entityliving);
                pathFuture.complete(path);
            });
            pendingNavigation = pathFuture;
            return true;
        }
    }
}