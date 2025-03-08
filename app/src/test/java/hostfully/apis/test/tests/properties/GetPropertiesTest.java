package hostfully.apis.test.tests.properties;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import hostfully.apis.test.api.PropertiesApi;
import hostfully.apis.test.base.BaseTest;
import hostfully.apis.test.pojos.Properties;
import hostfully.apis.test.utils.AliasUtils;
import hostfully.apis.test.utils.CustomTestWatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


@ExtendWith(CustomTestWatcher.class)
public class GetPropertiesTest extends BaseTest{

    public String alias;
    public String dateNow;
    public String username;
    public String password;
    public String propertyId;
    public Response response;


    @BeforeEach
    public void beforeEach(){
        alias = new AliasUtils().generateRandomAlias();
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        Properties property = new Properties(alias, "US");
        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        response = propertiesApi.createProperty(property, username, password);
        
        response.then()
            .extract().response();
    }

    @Test
    @DisplayName("Hostfully APis: Get a property by ID with success")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void getPropertyByIdSuccess(){

        JsonPath jsonPath = response.jsonPath();

        propertyId = jsonPath.get("id");
        
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.getPropertyById(propertyId, username, password);
        
        response.then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/property.json"))
            .body("id", equalTo(propertyId))
            .body("alias",equalTo(alias))
            .body("countryCode",equalTo("US"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
            jsonPath = response.jsonPath();

        Assertions.assertNotNull(jsonPath.get("id"));
        Assertions.assertNotNull(jsonPath.get("createdAt"));
        
    }

    @Test
    @DisplayName("Hostfully APis: Attempt to get a property with invalid id (non existent property)")
    @Tags({
        @Tag("regression"), @Tag("properties")
    })
    public void attemptGetPropertyInvalidId(){

        JsonPath jsonPath = response.jsonPath();

        propertyId = jsonPath.get("id");
        
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.getPropertyById("bbbba2-a6e4-47b9-b93e-29050f3ab2d0", username, password);
        
        response.then()
            .statusCode(404)
            .body("error", equalTo("Not Found"))
            .body("status", equalTo(404))
            .extract().response();
        
    }


    @Test
    @DisplayName("Hostfully APis: Attempt to get a property with id invalid pattern(id or wrong string)")
    @Tags({
        @Tag("regression"), @Tag("properties")
    })
    public void attemptGetPropertyIdWrongIdStringType(){

        JsonPath jsonPath = response.jsonPath();

        propertyId = jsonPath.get("id");
        
        username = envConfig.getUsername();
        password = envConfig.getPassword();

        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.getPropertyById("1", username, password);
        
        response.then()
            .statusCode(400)
            .body("title", equalTo("Bad Request"))
            .body("status", equalTo(400))
            .body("detail", containsString("Failed to convert 'propertyId' with value: '1'"))
            .extract().response();
        
    }


    @Test
    @DisplayName("Hostfully APis: Attempt to create a Property with wrong credentials")
    @Tags({
        @Tag("regression"), @Tag("properties"), @Tag("sanity")
    })
    public void attemptGetPropertyWrongCredentials(){

        JsonPath jsonPath = response.jsonPath();

        propertyId = jsonPath.get("id");
        
        username = "test";
        password = "test";

        PropertiesApi propertiesApi =  new PropertiesApi(requestSpec);

        Response response = propertiesApi.getPropertyById(propertyId, username, password);
        
        response.then()
            .statusCode(401)
            .body("exception", equalTo("Bad credentials"))
            .body("error", equalTo("Unauthorized"))
            .body("message", equalTo("Error while authenticating your access"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
    }
    
}
