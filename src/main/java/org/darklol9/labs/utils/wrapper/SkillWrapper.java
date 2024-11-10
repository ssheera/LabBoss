package org.darklol9.labs.utils.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.darklol9.labs.struct.MiniBoss;
import org.darklol9.labs.struct.SkillType;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SkillWrapper {

    private SkillType skillType;

    private double chance;

    private double damage;

    private long cooldown;

    @Builder.Default
    private String displayName = "";

    @Builder.Default
    private String message = "";

    @Singular
    private Map<String, Object> options;

//    private MiniBoss minion;

    public int getIntOption(String key, int defaultValue) {
        Number number = (Number) options.getOrDefault(key, defaultValue);
        return number.intValue();
    }

    public double getDoubleOption(String key, double defaultValue) {
        Number number = (Number) options.getOrDefault(key, defaultValue);
        return number.doubleValue();
    }

    public long getLongOption(String key, long defaultValue) {
        Number number = (Number) options.getOrDefault(key, defaultValue);
        return number.longValue();
    }

    public boolean getBooleanOption(String key, boolean defaultValue) {
        return (boolean) options.getOrDefault(key, defaultValue);
    }

    public String getStringOption(String key, String defaultValue) {
        return (String) options.getOrDefault(key, defaultValue);
    }

    public <T> T getOption(String minion, Object o) {
        return (T) options.getOrDefault(minion, (T) o);
    }
}
