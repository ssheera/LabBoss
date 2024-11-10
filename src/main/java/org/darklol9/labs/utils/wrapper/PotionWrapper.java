package org.darklol9.labs.utils.wrapper;

import com.massivecraft.massivecore.PotionEffectWrap;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

@Getter
@Builder
public class PotionWrapper {

    private String type;

    private int duration;

    private int amplifier;

    public PotionEffectWrap toPotionEffectWrap() {
        PotionEffectType pet = PotionEffectType.getByName(type);
        return new PotionEffectWrap(pet.getId(), amplifier, duration == -1 ? Integer.MAX_VALUE : duration, true, false);
    }
}
