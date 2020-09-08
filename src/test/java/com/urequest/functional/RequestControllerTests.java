package com.urequest.functional;

import io.restassured.http.Header;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import org.springframework.http.HttpStatus;

public class RequestControllerTests {
    private final String ENDPOINT = "/v1/requests";

    @Test
    public void post_request_happy_path_returns_ok() {
        given()
                .when()
                .body("{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIp\":\"123.234.56.78\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void post_request_returns_bad_request_when_json_is_malformed() {
        given()
                .when()
                .body("{malformed json}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_request_returns_bad_request_when_ip_address_is_blacklisted() {
        given()
                .when()
                .body("{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"21.307.064.33\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_request_returns_bad_request_when_user_agent_is_blacklisted() {
        given()
                .when()
                .header(new Header("User-Agent", "A6-Indexer"))
                .body("{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"22.307.064.33\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_request_returns_bad_request_when_customer_does_not_exist() {
        given()
                .when()
                .body("{\"customerId\":10000000,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"22.307.064.33\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_request_returns_bad_request_when_customer_field_is_missing() {
        given()
                .when()
                .body("{\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"22.307.064.33\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_request_returns_bad_request_when_customer_is_not_active() {
        given()
                .when()
                .body("{\"customerId\":3,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"22.307.064.33\",\"timestamp\":1500000000}")
                .contentType("application/json")
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
