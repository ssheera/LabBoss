package org.darklol9.labs.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.darklol9.labs.utils.wrapper.ItemWrapper;
import org.darklol9.labs.utils.wrapper.PotionWrapper;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbstractBoss {

    protected String id;

    protected String displayName;

    protected EntityType entityType;

    protected double maxHealth;

    protected double damageReduction;

    protected double attackDamage;

    protected double attackSpeed;

    protected double rangedAttackRange;

    protected ItemWrapper helmet;
    protected ItemWrapper chestplate;
    protected ItemWrapper leggings;
    protected ItemWrapper boots;

    protected ItemWrapper mainHand;

    protected List<PotionWrapper> effects;

}
