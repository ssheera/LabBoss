package org.darklol9.labs.utils.helper;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class TimeHelper {

    public String getTimeString(long milliseconds) {

        ChronoUnit store = null;

        ChronoUnit[] units = new ChronoUnit[]{
                ChronoUnit.WEEKS,
                ChronoUnit.DAYS,
                ChronoUnit.HOURS,
                ChronoUnit.MINUTES,
                ChronoUnit.SECONDS
        };

        for (ChronoUnit unit : units) {
            Duration duration = unit.getDuration();
            long unitMillis = duration.toMillis();
            if (milliseconds >= unitMillis) {
                store = unit;
                break;
            }
        }

        if (store == null) return "0s";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < units.length; i++) {
            ChronoUnit unit = units[i];
            String unitString = unit.toString().toLowerCase().substring(0, 1);
            int compare = unit.compareTo(store);
            if (compare > 0) continue;
            Duration duration = unit.getDuration();
            long unitMillis = duration.toMillis();
            if (milliseconds >= unitMillis) {
                long timeValue = milliseconds / unitMillis;
                switch (compare) {
                    case 0:
                        if (timeValue > 0) {
                            if (sb.length() > 0) sb.append(" ");
                            sb.append(timeValue).append(unitString);
                        }
                        milliseconds %= unitMillis;
                        if (units.length > i + 1) {
                            ChronoUnit next = units[i + 1];
                            long nextMillis = next.getDuration().toMillis();
                            long nextValue = milliseconds / nextMillis;
                            if (nextValue > 0) {
                                if (sb.length() > 0) sb.append(" ");
                                sb.append(nextValue).append(next.toString().toLowerCase().charAt(0));
                            }
                        }
                        return sb.toString();
                    case -1:
                    default:
                        if (timeValue > 0) {
                            if (sb.length() > 0) sb.append(" ");
                            sb.append(timeValue).append(unitString);
                        }
                        return sb.toString();
                }
            }
        }

        return "0s";
    }

}
