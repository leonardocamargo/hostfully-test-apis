package hostfully.apis.test.utils;

import java.time.LocalDateTime;

public class AliasUtils {

    public String alias;

    public String generateRandomAlias(){
        alias = "automation-apis" + LocalDateTime.now();
        return alias;
    }
    
}
