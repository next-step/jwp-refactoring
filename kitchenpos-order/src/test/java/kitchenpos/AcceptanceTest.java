package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(classes = OrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/db/sql/truncate.sql")
public abstract class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(path)
            .then().log().all()
            .extract();
    }

    public static <T> ExtractableResponse<Response> post(String path, T request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(path)
            .then().log().all()
            .extract();
    }

    public static <T> ExtractableResponse<Response> put(String path, T request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return RestAssured
            .given().log().all()
            .when().delete(path)
            .then().log().all()
            .extract();
    }
}
