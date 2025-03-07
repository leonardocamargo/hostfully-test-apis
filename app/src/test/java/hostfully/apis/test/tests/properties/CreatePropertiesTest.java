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
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/property.json"))
            .body("alias", equalTo(property.getAlias()))
            .body("countryCode", equalTo(property.getCountryCode()))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));
            Assertions.assertNotNull(jsonPath.get("createdAt"));
        
    }

    @Test
    @DisplayName("Hostfully APis: Create a property with different country code: BR")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void createPropertyDifferentCountryCode(){

        alias = new AliasUtils().generateRandomAlias();
        dateNow = new DateUtils().getCurrentDate();
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        Properties property = new Properties(alias, "BR");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.createProperty(property, username, password);
        
        response.then()
            .statusCode(201)
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/property.json"))
            .body("alias", equalTo(property.getAlias()))
            .body("countryCode", equalTo(property.getCountryCode()))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));
            Assertions.assertNotNull(jsonPath.get("createdAt"));
        
    }

    @Test
    @DisplayName("Hostfully APis: Attempt to create a Property with alias already existed")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void attemptCreatePropertyAliasAlreadyExisted(){

        alias = new AliasUtils().generateRandomAlias();
        dateNow = new DateUtils().getCurrentDate();
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        Properties property1 = new Properties(alias, "US");
        Properties property2 = new Properties(alias, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        //we create a property to ensure we have the alias on DB
        propertiesApi.createProperty(property1, username, password);
        
        Response response = propertiesApi.createProperty(property2, username, password);

        response.then()
            .statusCode(409)
            .body("status", equalTo(409))
            .body("title", equalTo("Conflict"))
            .body("detail", equalTo("Property(Alias) already existed."))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
    }


    @Test
    @DisplayName("Hostfully APis: Attempt to create a Property with invalid country code")
    @Tags({
        @Tag("properties"), @Tag("sanity")
    })
    public void attemptCreatePropertyInvalidCountryCode(){

        alias = new AliasUtils().generateRandomAlias();
        dateNow = new DateUtils().getCurrentDate();
        username = envConfig.getUsername();
        password = envConfig.getPassword();

    
        Properties property = new Properties(alias, "TEST_CODE");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);
        
        Response response = propertiesApi.createProperty(property, username, password);

        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("detail", equalTo("Invalid countryCode."))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
    }
    
}
