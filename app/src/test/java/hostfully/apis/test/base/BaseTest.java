package hostfully.apis.test.base;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import hostfully.apis.test.utils.EnvironmentConfig;

public abstract class BaseTest {

    protected RequestSpecification requestSpec;
    protected Properties configProps;
    protected EnvironmentConfig envConfig;

    @BeforeEach
    public void setUp(){
        //load properties where the default is qa
        String env = System.getProperty("env", "qa");
        envConfig = new EnvironmentConfig(env);
        configProps = envConfig.getAllProperties();

        requestSpec = new RequestSpecBuilder()
            .setBaseUri(envConfig.getBaseUri())
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();
    }
}
