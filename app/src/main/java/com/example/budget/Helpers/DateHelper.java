package com.example.budget.Helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public static long LocalDateToUnixTimestamp(LocalDate localDate){
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0));
        long epoch = localDate.atStartOfDay(zoneId).toEpochSecond();
        return epoch;
    }
    public static String localDateToFormattedString(LocalDate localDate){
        return DateHelper.localDateToFormattedString(localDate,"dd LLLL yyyy");
    }
    public static String localDateToFormattedString(LocalDate localDate, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDate);
    }
}
