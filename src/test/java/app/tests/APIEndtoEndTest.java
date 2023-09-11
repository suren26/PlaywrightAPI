package app.tests;

import app.data.BookingData;
import app.data.BookingDataBuilder;
import app.data.BookingDates;
import app.data.PartialBookingData;
import authentication.Token;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import manager.RequestManager;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static app.data.BookingDataBuilder.getBookingData;
import static app.data.BookingDataBuilder.getPartialBookingData;
import static authentication.TokenBuilder.getToken;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class APIEndtoEndTest extends BaseTest {
    private int bookingId;
    private BookingData bookingData;
    private BookingDates bookingDates;
    private static String token;

    private RequestManager manager;

    @BeforeClass
    public void setupTest() {
        System.out.println("Set up");
        this.bookingData = BookingDataBuilder.getBookingData();
        this.bookingDates = this.bookingData.getBookingdates();
        this.manager = new RequestManager();
        this.manager.createPlaywright();
        final String baseUrl = "https://restful-booker.herokuapp.com";
        final Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Accept", "application/json");
        this.manager.setApiRequestContext(baseUrl, headers);
        generateTokenTest();
    }

    public void generateTokenTest() {
        System.out.println("generateTokenTest");
        final Token tokenData = getToken();
        final APIResponse response = this.manager.postRequest("/auth", RequestOptions.create()
                .setData(tokenData));
        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        final String tokenValue = responseObject.getString("token");
        assertNotNull(tokenValue);
        this.token = tokenValue;
        System.out.println("generateTokenTest End :" + this.token);
    }

    @Test
    public void HealthCheck() {
        System.out.println("HealthCheck");
        final APIResponse response = this.manager.getRequest("/ping");
        System.out.println(response.status());
        System.out.println("HealthCheck End");
    }

    @Test
    public void createBookingTest() {
        System.out.println("createBookingTest ");
        final APIResponse response = this.manager.postRequest("/booking", RequestOptions.create()
                .setData(this.bookingData)
                .setHeader("Cookie", "token=" + this.token));
        assertEquals(response.status(), 200);
        final JSONObject responseObject = new JSONObject(response.text());
        assertNotNull(responseObject.get("bookingid"));
        final JSONObject bookingObject = responseObject.getJSONObject("booking");
        final JSONObject bookingDatesObject = bookingObject.getJSONObject("bookingdates");
        assertEquals(this.bookingData.getFirstname(), bookingObject.get("firstname"));
        assertEquals(this.bookingData.getBookingdates()
                .getCheckin(), bookingDatesObject.get("checkin"));
        this.bookingId = responseObject.getInt("bookingid");
        getBooking(this.bookingId);
        System.out.println("createBookingTest End - Booking ID: " + this.bookingId);
    }

    public void getBooking(int bookingid) {
        final APIResponse response = this.manager.getRequest("/booking/" + bookingid);
        assertEquals(response.status(), 200);
        System.out.println(response.text());
    }

    @Test(dependsOnMethods = {"createBookingTest"})
    public void updateBookingTest() {
        System.out.println("updateBookingTest");
        final BookingData updateBookingData = getBookingData();
        final APIResponse response = this.manager.putRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setData(updateBookingData)
                .setHeader("Cookie", "token=" + this.token));
        assertEquals(response.status(), 200);
        final JSONObject responseObject = new JSONObject(response.text());
        final JSONObject bookingDatesObject = responseObject.getJSONObject("bookingdates");
        assertEquals(updateBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(updateBookingData.getBookingdates()
                .getCheckout(), bookingDatesObject.get("checkout"));
        getBooking(this.bookingId);
        System.out.println("updateBookingTest End - Booking ID: " + this.bookingId);
    }
    @Test(dependsOnMethods = {"updateBookingTest"})
    public void updatePartialBookingTest() {
        System.out.println("updatePartialBookingTest");
        final PartialBookingData partialBookingData = getPartialBookingData();
        final APIResponse response = this.manager.patchRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setData(partialBookingData)
                .setHeader("Cookie", "token=" + this.token));

        assertEquals(response.status(), 200);
        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(partialBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(partialBookingData.getTotalprice(), responseObject.get("totalprice"));
        getBooking(this.bookingId);
        System.out.println("updatePartialBookingTest End - Booking ID: " + this.bookingId);
    }
    @Test(dependsOnMethods = {"updatePartialBookingTest"})
    public void deleteBookingTest() {
        System.out.println("deleteBookingTest");
        final APIResponse response = this.manager.deleteRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setHeader("Cookie", "token=" + this.token));
        assertEquals(response.status(), 201);
        System.out.println("deleteBookingTest End - Booking ID: " + this.bookingId);
    }

}
