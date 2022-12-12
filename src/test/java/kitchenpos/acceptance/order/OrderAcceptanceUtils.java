package kitchenpos.acceptance.order;

import static kitchenpos.acceptance.menu.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.ordertable.OrderTableAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menu.MenuId;
import kitchenpos.acceptance.ordertable.OrderTableId;

public class OrderAcceptanceUtils {
    private OrderAcceptanceUtils() {}

    public static ExtractableResponse<Response> 주문_등록_요청(
        OrderTableId orderTableId, List<OrderLineItemParam> orderLineItems) {
        Map<String, Object> body = new HashMap<>();
        body.put("orderTableId", orderTableId.value());
        body.put("orderLineItems", orderLineItems);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    public static void 주문_등록_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_등록_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }

    public static void 주문_목록_조회_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static OrderId 주문_ID_추출(ExtractableResponse<Response> response) {
        return new OrderId(response.jsonPath().getLong("id"));
    }

    public static OrderId 주문_등록되어_있음(OrderTableId orderTableId) {
        MenuId 메뉴_ID = 메뉴_등록되어_있음();
        List<OrderLineItemParam> 주문_항목_목록 = Arrays.asList(new OrderLineItemParam(메뉴_ID, 1));
        return 주문_ID_추출(주문_등록_요청(orderTableId, 주문_항목_목록));
    }

    public static OrderId 주문_등록되어_있음() {
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, false));
        return 주문_등록되어_있음(주문_테이블_ID);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(OrderId orderId, String orderStatus) {
        Map<String, Object> body = new HashMap<>();
        body.put("orderStatus", orderStatus);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put("/api/orders/" + orderId.value() + "/order-status")
            .then().log().all()
            .extract();
    }

    public static void 주문_상태_변경_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
