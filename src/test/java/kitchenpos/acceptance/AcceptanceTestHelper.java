package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AcceptanceTestHelper {

    static void assertOkStatus(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static void assertCreatedStatus(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    static void assertNoContentStatus(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static void assertInternalServerErrorStatus(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    static <T> ExtractableResponse<Response> post(String url, T requestBody) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().post(url)
            .then().log().all()
            .extract();
    }

    static ExtractableResponse<Response> get(String url) {
        return RestAssured
            .given().log().all()
            .when().get(url)
            .then().log().all()
            .extract();
    }

    static <T> ExtractableResponse<Response> put(String url, T requestBody) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().put(url)
            .then().log().all()
            .extract();
    }

    static ExtractableResponse<Response> delete(String url) {
        return RestAssured
            .given().log().all()
            .when().delete(url)
            .then().log().all()
            .extract();
    }
}
