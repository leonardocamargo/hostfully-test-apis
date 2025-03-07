package hostfully.apis.test.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    
    public String date;

    public String getCurrentDate(){
        OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);
        
        //formating using the pattern ISO 8601
        String date = nowUtc.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);        
        
        return date;
    }

    public String getCurrentDateWithoutHours(){
        date = java.time.LocalDate.now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_LOCAL_DATE);

        return date;
    }

    public String getPastDate() {
        return getPastDate(30); // default days 30 
    }

    public String getPastDate(int daysAgo) {
        return LocalDate.now(ZoneOffset.UTC)
                .minusDays(daysAgo)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getFutureDate() {
        return getFutureDate(30); // default days: 30 days ahead
    }

    public String getFutureDate(int daysAhead) {
        return LocalDate.now(ZoneOffset.UTC)
                .plusDays(daysAhead)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }


    
}
