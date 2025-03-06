package hostfully.apis.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {

    private Properties configProps;

    public EnvironmentConfig(String env) {
        configProps = new Properties();
        String filename = env + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                System.err.println("File not found: " + filename);
            } else {
                configProps.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getBaseUri() {
        return configProps.getProperty("baseUri", "https://qa-assessment.svc.hostfully.com");
    }

    public String getUsername() {
        return configProps.getProperty("username", "user");
    }

    public String getPassword() {
        return configProps.getProperty("password", "pass");
    }
    
    public Properties getAllProperties() {
        return configProps;
    }
}
