package kitchenpos.common.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;

import java.util.Map;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static ExtractableResponse<Response> ofRequest(Method method, String path, Map<String, ?> pathParams, Object body) {
        return extractableResponse(given(pathParams, body), method, path);
    }

    public static ExtractableResponse<Response> ofRequest(Method method, String path, Map<String, ?> pathParams) {
        return extractableResponse(given(pathParams), method, path);
    }

    public static ExtractableResponse<Response> ofRequest(Method method, String path, Object body) {
        return extractableResponse(given(body), method, path);
    }

    public static ExtractableResponse<Response> ofRequest(Method method, String path) {
        return extractableResponse(given(), method, path);
    }

    private static ExtractableResponse<Response> extractableResponse(RequestSpecification given, Method method, String path) {
        return given
                .when()
                .request(method, path)
                .then().log().all()
                .extract();
    }

    private static RequestSpecification given() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private static RequestSpecification given(Map<String, ?> pathParams) {
        return given().pathParams(pathParams);
    }

    private static RequestSpecification given(Object body) {
        return given().body(body);
    }

    private static RequestSpecification given(Map<String, ?> pathParams, Object body) {
        return given()
                .pathParams(pathParams)
                .body(body);
    }
}
