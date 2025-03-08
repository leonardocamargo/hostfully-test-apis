package hostfully.apis.test.api;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import hostfully.apis.test.pojos.Bookings;


/**
 * This class encapsulates API calls related to "Bookings".
 * Instead of extending BaseTest, we use composition by receiving a pre-configured
 * RequestSpecification (from BaseTest) via the constructor. This avoids duplicating
 * the common configuration (baseUri, headers, filters, etc.) and ensures that the
 * RequestSpecification is properly initialized by BaseTest's @BeforeEach method.
 */

public class BookingsApi {


    private RequestSpecification requestSpec;

    public BookingsApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response createBooking(Bookings booking, String propertyId, String username, String password) {
        return given()
            .log().all()
            .spec(requestSpec)
            .auth().preemptive().basic(username, password)
            .body(booking)
            .when()
            .post("/bookings")
            .then()
            .extract().response();
    }

    public Response getBookingById(String propertyId, String username, String password) {
        return given()
            .spec(requestSpec)
            .auth().preemptive().basic(username, password)
            .pathParam("propertyId", propertyId)
            .when()
            .get("/properties/{propertyId}")
            .then()
            .extract().response();
    }


    public Response getProperties(String propertyId, String username, String password) {
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
