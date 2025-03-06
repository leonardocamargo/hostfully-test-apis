package hostfully.apis.test.utils;

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
}
