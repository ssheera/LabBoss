package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class LootWrapper {

    private String id;

    @Singular
    private List<String> commands;
}
