package hostfully.apis.test.base;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.*;

public abstract class BaseTest {
    

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    protected Properties configProps;

    @BeforeEach
    public void setUp(){

        requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://qa-assessment.svc.hostfully.com")
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();
    }

}
