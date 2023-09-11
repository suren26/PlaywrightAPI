package app.data;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;

@Builder
@Getter

public class BookingDates {
    private String checkin;
    private String checkout;
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("\n checkin",checkin)
                .append("\n checkout",checkout)
                .toString();
    }
}
