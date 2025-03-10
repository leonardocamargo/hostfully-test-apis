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
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.*;

@ExtendWith(CustomTestWatcher.class)
public class CreatePropertiesTest extends BaseTest {

    public String alias;
    public String dateNow;
    public String username;
    public String password;

    @BeforeEach
    public void beforeEach(){
        username = envConfig.getUsername();
        password = envConfig.getPassword();
    }

    @Test
    @DisplayName("Hostfully APis: Create a property with success")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void createPropertySuccess(){

        alias = new AliasUtils().generateRandomAlias();

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

    @Test
    @DisplayName("Hostfully APis: Attempt to create a Property without passing an alias")
    @Tags({
        @Tag("properties"), @Tag("sanity")
    })
    public void attemptCreatePropertyWithoutAlias(){

        Properties property = new Properties(null, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);
        
        Response response = propertiesApi.createProperty(property, username, password);

        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Property alias is required"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
    }


    @Test
    @DisplayName("Hostfully APis: Attempt to create a Property with wrong credentials")
    @Tags({
        @Tag("properties"), @Tag("sanity"), @Tag("regression")
    })
    public void attemptCreatePropertyWrongCredentials(){

        alias = new AliasUtils().generateRandomAlias();
        username = "test";
        password = "test";
    
        Properties property = new Properties(alias, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);
        
        Response response = propertiesApi.createProperty(property, username, password);

        response.then()
            .statusCode(401)
            .body("exception", equalTo("Bad credentials"))
            .body("error", equalTo("Unauthorized"))
            .body("message", equalTo("Error while authenticating your access"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
    }

    @Test
    @DisplayName("Hostfully APis: Attempt to create a property invalid URL")
    @Tags({
        @Tag("properties"), @Tag("sanity")
    })
    public void attemptCreatePropertyInvalidURL(){

        alias = new AliasUtils().generateRandomAlias();

        Properties property = new Properties(alias, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = RestAssured.given()
            .spec(requestSpec)
            .auth().preemptive().basic(username, password)
            .body(property)
            .when()
            .post("/propertiesss")
            .then()
            .statusCode(404)
            .body("error", equalTo("Not Found"))
            .body("status", equalTo(404))
            .extract().response();


    }
    
}
