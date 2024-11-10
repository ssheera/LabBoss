package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class MenuWrapper {

    private String title;
    private int size;

    @Singular("row")
    private List<String> design;

    @Singular("item")
    private Map<String, MenuItemWrapper> items;

}
