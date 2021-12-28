package kitchenpos.utils;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StatusCodeCheckUtil {
    private StatusCodeCheckUtil() {
    }

    public static void ok(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    public static void created(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.CREATED);
    }

    public static void noContent(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static void assertHttpStatus(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
