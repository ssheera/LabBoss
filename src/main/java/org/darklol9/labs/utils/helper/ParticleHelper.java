package org.darklol9.labs.utils.helper;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;
import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;

@UtilityClass
public class ParticleHelper {

    public void spawnParticleLine(Location locA, Location locB, ParticleEffect effect, ParticleEffect.ParticleData data) {

        if (locA.getWorld() != locB.getWorld())
            throw new IllegalArgumentException("Locations must be in the same world");

        double distance = locA.distance(locB);

        for (double i = 0; i < distance; i += 0.1) {
            Location loc = locA.clone().add(locB.clone().subtract(locA).toVector().normalize().multiply(i));
            if (data == null) effect.display(0, 0, 0, 0, 5, loc, 100);
            else effect.display(data, 0, 0, 0, 0, 5, loc, 100);
        }

    }

    public static void spawnParticleRadius(Location location, double radius, ParticleEffect effect) {
        for (int i = 0; i < 360; i += 2) {
            double angle = i * Math.PI / 180;
            double x = MathHelper.cos((float) angle) * radius;
            double z = MathHelper.sin((float) angle) * radius;
            Location loc = location.clone().add(x, 0, z);
            effect.display(0, 0, 0, 0, 1, loc, 100);
        }
    }
}
