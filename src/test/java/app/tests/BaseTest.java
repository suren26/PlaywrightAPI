package app.tests;

import manager.RequestManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected RequestManager manager;

    @BeforeTest
    public void setupBase() {
        System.out.println("Set-Up Base Test");
        this.manager = new RequestManager();
        this.manager.createPlaywright();
        final String baseUrl = "https://restful-booker.herokuapp.com";
        final Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Accept", "application/json");
        this.manager.setApiRequestContext(baseUrl, headers);
    }

    @AfterTest
    public void tearDown() {
        this.manager.disposeAPIRequestContext();
        this.manager.closePlaywright();
        System.out.println("Tear Down Base Test");
    }
}
