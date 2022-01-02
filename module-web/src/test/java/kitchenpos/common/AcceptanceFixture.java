package kitchenpos.common;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.*;

import io.restassured.*;
import io.restassured.response.*;

public class AcceptanceFixture {

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object param) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object param, Object... pathParam) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .put(path, pathParam)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object... pathParam) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path, pathParam)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String path, Object... pathParam) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path, pathParam)
                .then().log().all()
                .extract();
    }

    public static void OK_응답_잘_받았음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void CREATE_응답_잘_받음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void BAD_REQUEST_응답_잘_받았음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 응답_NO_CONTENT(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
