package hostfully.apis.test.tests.properties;

import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.number.OrderingComparison.lessThan;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import hostfully.apis.test.base.BaseTest;
import hostfully.apis.test.pojos.Properties;
import hostfully.apis.test.utils.AliasUtils;
import hostfully.apis.test.utils.CustomTestWatcher;
import hostfully.apis.test.utils.DateUtils;
import io.restassured.*;
import io.restassured.response.*;

@ExtendWith(CustomTestWatcher.class)
public class CreatePropertiesTest extends BaseTest {

    public String alias;
    public String dateNow;
    public String username;
    public String password;

    @Test
    @DisplayName("Hostfully APis: Create a property with success")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void createPropertySuccess(){

        alias = new AliasUtils().generateRandomAlias();
        dateNow = new DateUtils().getCurrentDate();
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        Properties property = new Properties(alias, "US",dateNow);

        Response response = RestAssured.given()
            .spec(requestSpec)
            //we need to pass preemptive because the api doesnt return a challenge, requiring the basic auth in the first request
            .auth().preemptive().basic(username, password)
            .body(property)
            .log().all()
            .when()
            .post("/properties")
            .then()
            .statusCode(201)
            .body("alias", equalTo(property.getAlias()))
            .time(lessThan(5L), TimeUnit.SECONDS)
            .extract().response();


    }

    
}
