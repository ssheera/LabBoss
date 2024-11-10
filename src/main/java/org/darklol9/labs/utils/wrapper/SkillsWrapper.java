package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class SkillsWrapper {

    @Builder.Default
    private double overallChance = 1.0;

    @Singular
    private List<SkillWrapper> skills;

}
