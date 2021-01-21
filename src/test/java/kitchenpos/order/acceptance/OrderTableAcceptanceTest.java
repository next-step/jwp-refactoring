package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@DisplayName("주문 테이블 관련 기능")
public class OrderTableAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", 0);
        params.put("empty", true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(params);

        // then
        주문_테이블_둥록됨(response);
    }

    @DisplayName("주문 테이블 목록")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(response, HttpStatus.OK);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all().extract();
        return response;
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }

    private void 주문_테이블_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
