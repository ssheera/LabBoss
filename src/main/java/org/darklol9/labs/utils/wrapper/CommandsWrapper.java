package org.darklol9.labs.utils.wrapper;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommandsWrapper {

    @Builder.Default
    private List<String> onSpawn = MUtil.list();

    @Builder.Default
    private List<String> onDeath = MUtil.list();

}
