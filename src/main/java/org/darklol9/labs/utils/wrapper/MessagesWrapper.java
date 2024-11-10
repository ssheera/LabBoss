package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class MessagesWrapper {

    @Singular("spawn")
    private List<String> onSpawn;

    @Singular("death")
    private List<String> onDeath;

    private String onDeathFormat;

}
