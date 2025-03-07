package hostfully.apis.test.tests.properties;

import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.number.OrderingComparison.lessThan;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import hostfully.apis.test.api.PropertiesApi;
import hostfully.apis.test.base.BaseTest;
import hostfully.apis.test.pojos.Properties;
import hostfully.apis.test.utils.AliasUtils;
import hostfully.apis.test.utils.CustomTestWatcher;
import hostfully.apis.test.utils.DateUtils;
import io.restassured.path.json.JsonPath;
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

        Properties property = new Properties(alias, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.createProperty(property, username, password);
        
        response.then()
            .statusCode(201)
            validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/property.json"))
            .body("alias", equalTo(property.getAlias()))
            .body("countryCode", equalTo(property.getCountryCode()))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));
            Assertions.assertNotNull(jsonPath.get("createdAt"));
        
    }

    
}
