package hostfully.apis.test.pojos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endDate;

    private String status;
    private Guest guest;
    private String propertyId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Guest{
        private String firstName;
        private String lastName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;

    }
    
}
