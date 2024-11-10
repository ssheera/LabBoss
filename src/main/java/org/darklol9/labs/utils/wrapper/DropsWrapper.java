package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class DropsWrapper {

    @Builder.Default
    private boolean natural = false;

    @Singular("loot")
    private List<String> loot;

}
