package hostfully.apis.test.tests.properties;

import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.number.OrderingComparison.lessThan;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import hostfully.apis.test.base.BaseTest;
import hostfully.apis.test.pojos.Properties;
import hostfully.apis.test.utils.CustomTestWatcher;
import io.restassured.*;
import io.restassured.response.*;

@ExtendWith(CustomTestWatcher.class)
public class CreatePropertiesTest extends BaseTest {

    @Test
    @DisplayName("Hostfully APis: Create a property with success")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void createPropertySuccess(){

        Properties property = new Properties("tes22tPs22rop2ertyss2", "US","2025-03-01T18:43:20.896Z");

        Response response = RestAssured.given()
            .spec(requestSpec)
            //we need to pass preemptive because the api doesnt return a challenge, requiring the basic auth in the first request
            .auth().preemptive().basic("candidate@hostfully.com", "NaX5k1wFadtkFf")
            .body(property)
            .when()
            .post("/properties")
            .then()
            .body("alias", equalTo(property.getAlias()))
            .statusCode(201)
            .time(lessThan(5L), TimeUnit.SECONDS)
            .extract().response();


    }

    
}
