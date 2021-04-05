package net.buzz.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static String formatDHMS(long milliseconds) {
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        milliseconds -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        StringBuilder builder = new StringBuilder();
        if (days > 0L)
            builder.append(String.valueOf(days) + "d");
        if (hours > 0L) {
            builder.append(" ");
            builder.append(String.valueOf(hours) + "h");
        }
        if (minutes > 0L) {
            builder.append(" ");
            builder.append(String.valueOf(minutes) + "m ");
        }
        if (seconds > 0L) {
            builder.append("");
            builder.append(String.valueOf(seconds) + "s");
        }
        return builder.toString().trim();
    }
}
