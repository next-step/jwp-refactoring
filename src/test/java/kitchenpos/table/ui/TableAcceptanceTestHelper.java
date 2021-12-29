package kitchenpos.table.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractIntegerAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import table.dto.OrderTableResponse;

public class TableAcceptanceTestHelper {
    private TableAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 테이블_생성(Map<String, String> params) {
        // when
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/tables")
            .then().log().all().extract();
    }

    public static void 테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 테이블_조회() {
        // when
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all().extract();
    }

    public static AbstractIntegerAssert<?> 테이블_조회됨(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_갯수_예상과_일치(ExtractableResponse<Response> response, int expected) {
        List<OrderTableResponse> actual = response.jsonPath().getList(".", OrderTableResponse.class);

        assertThat(actual).hasSize(expected);
    }

    public static ExtractableResponse<Response> 테이블_손님_명수_변경(long id, Map<String, String> params) {
        // when
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + id + "/number-of-guests")
            .then().log().all().extract();
    }

    public static void 테이블_손님_명수_변경됨(ExtractableResponse<Response> response, int expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(expected);
    }

    public static ExtractableResponse<Response> 테이블_빈_테이블_여부_변경(long id, Map<String, String> params) {
        // when
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + id + "/empty")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 테이블_빈_테이블_여부_변경되어_있음(long id, String isEmpty) {
        Map<String, String> params = new HashMap<>();
        params.put("empty", isEmpty);

        return 테이블_빈_테이블_여부_변경(id, params);
    }

    public static void 테이블_빈_테이블_여부_변경됨(ExtractableResponse<Response> response, boolean expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertThat(result.isEmpty()).isEqualTo(expected);
    }

    public static void 테이블_손님_명수_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 테이블_빈_테이블_여부_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
