package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.dto.OrderResponse;

public class OrderAcceptanceTestHelper {
    private OrderAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Map<String, Object> params) {
        // when
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/orders")
            .then().log().all().extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_생성되어_있음(List<Map<String, Integer>> orderLineItem, int orderTableId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTableId);
        params.put("orderLineItems", orderLineItem);

        return 주문_생성_요청(params);
    }

    public static void 주문_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        // when
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all().extract();
    }

    public static void 주문_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_조회_갯수_예상과_일치(ExtractableResponse<Response> response, int expected) {
        List<OrderResponse> result = response.jsonPath().getList(".", OrderResponse.class);
        assertThat(result).hasSize(expected);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(long id, Map<String, String> params) {
        // when
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/orders/" + id + "/order-status")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경되어_있음(long id, String orderStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("orderStatus", orderStatus);

        return 주문_상태_변경_요청(id, params);
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_예상과_일치(ExtractableResponse<Response> response, String expected) {
        OrderResponse result = response.as(OrderResponse.class);
        assertThat(result.getOrderStatus()).isEqualTo(expected);
    }

    public static void 주문_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }
}
