package org.darklol9.labs.struct;

import lombok.Getter;
import org.darklol9.labs.skills.AbstractSkill;
import org.darklol9.labs.skills.active.*;
import org.darklol9.labs.skills.passive.DamageStackSkill;
import org.darklol9.labs.skills.passive.VampSkill;
import org.darklol9.labs.skills.special.*;

@Getter
public enum SkillType {

    // Active

    BLEED(BleedSkill.class),

    SELF_DESTRUCT(SelfDestructSkill.class),

    REJUVENATE(RejuvenateSkill.class),

    PUSH(PushSkill.class),

    PULL(PullSkill.class),

    LIGHTNING(LightningSkill.class),

    BLIND(BlindSkill.class),

    BLINK(BlinkSkill.class),

    IMMOLATE(ImmolateSkill.class),

    WEB(WebSkill.class),

    TRANSFUSION(TransfusionSkill.class),

    SHIELD(ShieldSkill.class),

    // Passive

    DAMAGESTACK(DamageStackSkill.class),

    VAMP(VampSkill.class),

    // Special

    MINIONS(MinionsSkill.class),

    HALLUCINATE(HallucinateSkill.class),

    DECIMATINGSMASH(DecimatingSmashSkill.class),

    HEARTBREAKER(HeartbreakerSkill.class);

    private final Class<? extends AbstractSkill> skill;

    SkillType(Class<? extends AbstractSkill> skill) {
        this.skill = skill;
    }

}
