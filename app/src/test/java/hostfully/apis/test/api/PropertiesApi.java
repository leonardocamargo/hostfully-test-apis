package hostfully.apis.test.api;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import hostfully.apis.test.pojos.Properties;


/**
 * This class encapsulates API calls related to "Properties".
 * Instead of extending BaseTest, we use composition by receiving a pre-configured
 * RequestSpecification (from BaseTest) via the constructor. This avoids duplicating
 * the common configuration (baseUri, headers, filters, etc.) and ensures that the
 * RequestSpecification is properly initialized by BaseTest's @BeforeEach method.
 */

public class PropertiesApi {


    private RequestSpecification requestSpec;

    public PropertiesApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response createProperty(Properties property, String username, String password) {
        return given()
            .spec(requestSpec)
            .auth().preemptive().basic(username, password)
            .body(property)
            .when()
            .post("/properties")
            .then()
            .extract().response();
    }

    public Response getPropertyById(String propertyId, String username, String password) {
        return given()
            .spec(requestSpec)
            .auth().preemptive().basic(username, password)
            .pathParam("id", propertyId)
            .when()
            .post("/properties")
            .then()
            .extract().response();
    }
}
