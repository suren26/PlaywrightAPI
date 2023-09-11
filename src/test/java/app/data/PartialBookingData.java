package app.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class PartialBookingData {
    private String firstname;
    private int totalprice;
}
