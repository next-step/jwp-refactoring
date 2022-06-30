package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptanceFactory {

    public static ExtractableResponse<Response> 주문_등록_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus("");
        order.setOrderLineItems(orderLineItems);
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when()
                .post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Order 주문, OrderStatus 주문상태) {
        주문.setOrderStatus(주문상태.name());
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문)
                .when()
                .put("/api/orders/{orderId}/order-status",주문.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_등록성공(ExtractableResponse<Response> 주문_등록_결과) {
        assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_등록실패(ExtractableResponse<Response> 주문_등록_결과) {
        assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_조회성공(ExtractableResponse<Response> 주문_조회_결과) {
        assertThat(주문_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_변경성공(ExtractableResponse<Response> 주문_변경_결과, OrderStatus 기대주문상태) {
        Order 변경된주문 = 주문_변경_결과.as(Order.class);
        assertAll(
                () -> assertThat(주문_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된주문.getOrderStatus()).isEqualTo(기대주문상태.name())
        );
    }
    public static void 주문_변경실패(ExtractableResponse<Response> 주문_변경_결과) {
        assertThat(주문_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
