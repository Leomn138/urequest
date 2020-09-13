package com.urequest.functional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

public class StatisticsControllerTests {
    private final String ENDPOINT = "/v1/statistics";

    @Test
    public void getStatistics_returns_correct_sum_when_no_stats_are_found() {
        given()
                .when()
                .get(ENDPOINT+"?customerId=5&date=2020-05-08")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                    containsString("{\"numberOfValidRequests\":0,\"numberOfInvalidRequests\":0,\"totalNumberOfRequests\":0,\"consolidated\":true,\"status\":\"OK\"}")
                );
    }

    @Test
    public void getStatistics_returns_correct_sum_when_stats_are_found() {
        given()
                .when()
                .get(ENDPOINT+"?customerId=1&date=2020-05-08")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        containsString("{\"numberOfValidRequests\":10,\"numberOfInvalidRequests\":11,\"totalNumberOfRequests\":21,\"consolidated\":true,\"status\":\"OK\"}")
                );
    }
}