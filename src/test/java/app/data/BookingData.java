package app.data;

import app.data.BookingDates;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;

@Builder
@Getter
public class BookingData
{
        private String firstname;
        private String lastname;
        private int totalprice;
        private boolean depositpaid;
        private BookingDates bookingdates;
        private String additionalneeds;
        @Override
        public String toString()
        {
                return new ToStringBuilder(this)
                        .append("\n firstname",firstname)
                        .append("\n lastname",lastname)
                        .append("\n totalprice",totalprice)
                        .append("\n depositpaid",depositpaid)
                        .append("\n bookingdates",bookingdates.toString())
                        .append("\n additionalneeds",additionalneeds)
                        .toString();
        }
}