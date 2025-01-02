package com.jeet.tradingapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static String formatDate() {
        Instant timestamp = Instant.parse("2024-07-12T10:30:00Z"); // Example Instant

        // Convert to LocalDateTime in a specific time zone (e.g., US/Eastern)
        ZoneId easternTimeZone = ZoneId.of("US/Eastern");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, easternTimeZone);

        // Format the LocalDateTime for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = localDateTime.format(formatter);

        System.out.println("Timestamp (UTC): " + timestamp);
        System.out.println("Local Time (US/Eastern): " + formattedTime);

        return formattedTime;
    }
}
