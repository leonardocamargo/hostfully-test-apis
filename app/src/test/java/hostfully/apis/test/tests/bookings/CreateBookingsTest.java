package hostfully.apis.test.tests.bookings;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import hostfully.apis.test.api.BookingsApi;
import hostfully.apis.test.api.PropertiesApi;
import hostfully.apis.test.base.BaseTest;
import hostfully.apis.test.pojos.Bookings;
import hostfully.apis.test.pojos.Bookings.Guest;
import hostfully.apis.test.pojos.Properties;
import hostfully.apis.test.utils.AliasUtils;
import hostfully.apis.test.utils.CustomTestWatcher;
import hostfully.apis.test.utils.DateUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


@ExtendWith(CustomTestWatcher.class)
public class CreateBookingsTest extends BaseTest {

    public String alias;
    public String dateNow;
    public String username;
    public String password;
    public String startDate;
    public String endDate;
    public String propertyId;
    public Response response;
    
    DateUtils dateUtils = new DateUtils();

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

        JsonPath jsonPath = response.jsonPath();
        propertyId = jsonPath.get("id");
    }

    @Test
    @DisplayName("Hostfully APis: Create a booking with success")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void createBookingSuccess(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(2);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(201)
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/bookings.json"))
            .body("propertyId", equalTo(propertyId))
            .body("startDate", equalTo(startDate))
            .body("endDate", equalTo(endDate))
            .body("status", equalTo("SCHEDULED"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));        
    }


    @Test
    @DisplayName("Hostfully APis: Create a booking with success Completed Status")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void createBookingSuccessCompletedStatus(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(2);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("COMPLETED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(201)
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/bookings.json"))
            .body("propertyId", equalTo(propertyId))
            .body("startDate", equalTo(startDate))
            .body("endDate", equalTo(endDate))
            .body("status", equalTo("SCHEDULED"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));
            // Assertions.assertNotNull(jsonPath.get("createdAt"));
        
    }


    @Test
    @DisplayName("Hostfully APis: Create a booking with success Cancelled Status")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void createBookingSuccessCancelledStatus(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(2);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("CANCELLED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(201)
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/bookings.json"))
            .body("propertyId", equalTo(propertyId))
            .body("startDate", equalTo(startDate))
            .body("endDate", equalTo(endDate))
            .body("status", equalTo("SCHEDULED"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));
    }

    @Test
    @DisplayName("Hostfully APis: Create a booking with start date and end date equals")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void createBookingSuccessEqualDates(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(1);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(201)
            //validating the schema
            .body(matchesJsonSchemaInClasspath("schemas/bookings.json"))
            .body("propertyId", equalTo(propertyId))
            .body("startDate", equalTo(startDate))
            .body("endDate", equalTo(endDate))
            .body("status", equalTo("SCHEDULED"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertNotNull(jsonPath.get("id"));        
    }


    @Test
    @DisplayName("Hostfully APis: Attempt to create a booking with invalid status")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void attemptCreateBookingInvalidStatus(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(1);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("TEST STATUS")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
        .statusCode(400)
        .body("status", equalTo(400))
        .body("title", equalTo("Bad Request"))
        .body("detail", equalTo("Invalid status."))
        .time(lessThan(10L), TimeUnit.SECONDS)
        .extract().response();
    }  
    
    @Test
    @DisplayName("Hostfully APis: Attempt to create a booking with invalid property (non existent)")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void attemptCreateBookingInvalidProperty(){
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(1);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId("1ebbdcaa-256b-479c-9a6e-4dd5bbadfbbb")
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Property not found"))
            .body("detail", equalTo("Property with identifier 1ebbdcaa-256b-479c-9a6e-4dd5bbadfbbb could not be found"))
            .time(lessThan(10L), TimeUnit.SECONDS)
        .extract().response();
    }    


    @Test
    @DisplayName("Hostfully APis: Attempt to create a booking with wrong credentials")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void attemptCreateBookingWrongCredentials(){

        username = "test";
        password = "test";
       
        startDate = dateUtils.getFutureDate(1);
        endDate = dateUtils.getFutureDate(1);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId("1ebbdcaa-256b-479c-9a6e-4dd5bbadfbbb")
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(401)
            .body("exception", equalTo("Bad credentials"))
            .body("error", equalTo("Unauthorized"))
            .body("message", equalTo("Error while authenticating your access"))
            .time(lessThan(10L), TimeUnit.SECONDS)
        .extract().response();
    }        

    @Test
    @DisplayName("Hostfully APis: Booking creation fails when end date is before start date")
    @Tags({
        @Tag("regression"), @Tag("bookings"), @Tag("sanity")
    })
    public void attemptCreateBookingEndDateBeforeStartDate(){
       
        startDate = dateUtils.getFutureDate(4);
        endDate = dateUtils.getFutureDate(1);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                .firstName("Test")
                .lastName("Leonardo")
                .dateOfBirth("1995-01-01")
            .build())
    .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);

        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("End date cannot be before the start date. Please check your booking dates."))
            .body("detail", equalTo("Property with identifier 1ebbdcaa-256b-479c-9a6e-4dd5bbadfbbb could not be found"))
            .time(lessThan(10L), TimeUnit.SECONDS)
        .extract().response();
    }        

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking without firstName")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingWithoutFirstName(){
   
        startDate = dateUtils.getFutureDate(4);
        endDate = dateUtils.getFutureDate(5);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName(null)  
                .lastName("Leonardo")
                .dateOfBirth("1995-01-01")
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Guest's first name needs to be supplied"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking without lastName")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingWithoutLastName(){
   
        startDate = dateUtils.getFutureDate(4);
        endDate = dateUtils.getFutureDate(5);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")  
                .lastName(null)
                .dateOfBirth("1995-01-01")
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Guest's last name needs to be supplied"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking without birth date")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingWithoutBirthDate(){
   
        startDate = dateUtils.getFutureDate(4);
        endDate = dateUtils.getFutureDate(5);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")  
                .lastName("Leonardo")
                .dateOfBirth(null)
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Guest's date birthday needs to be supplied"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking without start date")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingWithoutStartDate(){
   
        endDate = dateUtils.getFutureDate(5);

        Bookings bookings = Bookings.builder()
            .startDate(null)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")  
                .lastName("Leonardo")
                .dateOfBirth("1995-01-01")
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Booking start date must be supplied"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking without end date")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingWithoutEndDate(){
        
        startDate = dateUtils.getFutureDate(5);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(null)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")  
                .lastName("Leonardo")
                .dateOfBirth(null)
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Booking end date must be supplied"))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking with a date too future")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingDateTooFuture(){
        
        startDate = dateUtils.getFutureDate(30000);
        endDate = dateUtils.getFutureDate(30000);

        Bookings bookings = Bookings.builder()
            .startDate(startDate)
            .endDate(endDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")  
                .lastName("Leonardo Lima")
                .dateOfBirth("1995-01-01")
                .build())
            .build();

        BookingsApi bookingsApi =  new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Booking date invalid: Limit 10 years ahead."))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }

    @Test
    @DisplayName("Hostfully APIs: Booking creation fails when booking dates overlap with an existing booking")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingOverlappingDates() {

        String initialStartDate = dateUtils.getFutureDate(4);
        String initialEndDate = dateUtils.getFutureDate(6);
        
        Bookings initialBooking = Bookings.builder()
            .startDate(initialStartDate)
            .endDate(initialEndDate)
            .status("SCHEDULED")
            .propertyId(propertyId) 
            .guest(Bookings.Guest.builder()
                    .firstName("Test")
                    .lastName("Leonardo Lima")
                    .dateOfBirth("1995-01-01")
                    .build())
            .build();
        
        BookingsApi bookingsApi = new BookingsApi(requestSpec);
        Response initialResponse = bookingsApi.createBooking(initialBooking, propertyId, username, password);
        initialResponse.then().statusCode(201);
        
        //overlapping dates
        String overlappingStartDate = dateUtils.getFutureDate(5);
        String overlappingEndDate = dateUtils.getFutureDate(7);
        
        Bookings overlappingBooking = Bookings.builder()
            .startDate(overlappingStartDate)
            .endDate(overlappingEndDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Bookings.Guest.builder()
                    .firstName("Test")
                    .lastName("Leonardo Lima Overlapping")
                    .dateOfBirth("1990-02-02")
                    .build())
            .build();
        
        Response overlappingResponse = bookingsApi.createBooking(overlappingBooking, propertyId, username, password);
        
        overlappingResponse.then()
            .statusCode(409)
            .body("title", equalTo("Booking Conflict"))
            .body("detail", equalTo("Booking dates overlap with an existing booking"))
            .time(lessThan(10L), TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Hostfully APIs: Attempt to create a booking in the past")
    @Tags({ @Tag("regression"), @Tag("bookings"), @Tag("sanity") })
    public void attemptCreateBookingInPast() {
        
        String pastDate = dateUtils.getPastDate(1); 

        Bookings bookings = Bookings.builder()
            .startDate(pastDate)
            .endDate(pastDate)
            .status("SCHEDULED")
            .propertyId(propertyId)
            .guest(Guest.builder()
                .firstName("Test")
                .lastName("Booking Past")
                .dateOfBirth("1995-01-01")
                .build())
            .build();

        BookingsApi bookingsApi = new BookingsApi(requestSpec);
        Response response = bookingsApi.createBooking(bookings, propertyId, username, password);
        
        response.then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("title", equalTo("Validation Error"))
            .body("detail", equalTo("Validation failed"))
            .body("errors[0].defaultMessage", equalTo("Booking date invalid: Cannot create booking in the past."))
            .time(lessThan(10L), TimeUnit.SECONDS)
            .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("errors"));
    }


}
