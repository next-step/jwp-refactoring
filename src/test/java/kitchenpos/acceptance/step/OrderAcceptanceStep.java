package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceStep {

    public static OrderResponse 주문_등록_되어_있음(long tableId, long menuId, int quantity) {
        return 주문_등록_요청(tableId, menuId, quantity).as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(long tableId,
        long menuId, int quantity) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new OrderRequest(tableId,
                Collections.singletonList(new OrderLineItemRequest(menuId, quantity))))
            .when()
            .post("/api/orders")
            .then().log().all()
            .extract();
    }

    public static void 주문_등록_됨(ExtractableResponse<Response> response,
        int expectedQuantity, MenuResponse expectedMenu) {
        Order order = response.as(Order.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(order.getId()).isNotNull(),
            () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(order.getOrderedTime()).isEqualToIgnoringMinutes(LocalDateTime.now()),
            () -> assertThat(order.getOrderLineItems()).first()
                .satisfies(orderLineItem -> {
                    assertThat(orderLineItem.getSeq()).isNotNull();
                    assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenu.getId());
                    assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity);
                })
        );
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/api/orders")
            .then().log().all()
            .extract();
    }

    public static void 주문_목록_조회_됨(ExtractableResponse<Response> response,
        OrderResponse expectedOrder) {
        List<OrderResponse> orders = response.as(new TypeRef<List<OrderResponse>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(orders).first()
                .extracting(OrderResponse::getId)
                .isEqualTo(expectedOrder.getId())
        );
    }

    public static ExtractableResponse<Response> 주문_상태_수정_요청(long id, OrderStatus status) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new OrderStatusRequest(status.name()))
            .when()
            .put("/api/orders/{orderId}/order-status", id)
            .then().log().all()
            .extract();
    }

    public static void 주문_상태_수정_됨(ExtractableResponse<Response> response,
        OrderStatus expectedStatus) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderResponse.class))
                .extracting(OrderResponse::getOrderStatus)
                .isEqualTo(expectedStatus.name())
        );
    }

}
