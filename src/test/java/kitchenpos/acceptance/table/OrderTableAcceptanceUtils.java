package kitchenpos.acceptance.table;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderTableAcceptanceUtils {
    private OrderTableAcceptanceUtils() {}

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(long numberOfGuests, boolean empty) {
        Map<String, Object> body = new HashMap<>();
        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", empty);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_등록_요청_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_조회_요청_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static List<Map<String, Object>> 주문_테이블_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().get();
    }

    public static OrderTableId 주문_테이블_ID_추출(ExtractableResponse<Response> response) {
        return new OrderTableId(response.jsonPath().getLong("id"));
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(OrderTableId orderTableId, boolean empty) {
        Map<String, Object> body = new HashMap<>();
        body.put("empty", empty);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put("/api/tables/" + orderTableId.value() + "/empty")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_상태_변경_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(OrderTableId orderTableId, int numberOfGuests) {
        Map<String, Object> body = new HashMap<>();
        body.put("numberOfGuests", numberOfGuests);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put("/api/tables/" + orderTableId.value() + "/number-of-guests")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_손님_수_변경_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_손님_수_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
