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
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceStep {

    public static Order 주문_등록_되어_있음(OrderTable orderTable, Menu menu, int quantity) {
        return 주문_등록_요청(orderTable, menu, quantity).as(Order.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderTable orderTable,
        Menu menu, int quantity) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(orderCreateRequest(orderTable, orderLineItemRequest(menu, quantity)))
            .when()
            .post("/api/orders")
            .then().log().all()
            .extract();
    }

    public static void 주문_등록_됨(ExtractableResponse<Response> response,
        int expectedQuantity, Menu expectedMenu) {
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

    public static void 주문_목록_조회_됨(ExtractableResponse<Response> response, Order expectedOrder) {
        List<Order> orders = response.as(new TypeRef<List<Order>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(orders).first()
                .extracting(Order::getId)
                .isEqualTo(expectedOrder.getId())
        );
    }

    public static ExtractableResponse<Response> 주문_상태_수정_요청(long id, OrderStatus status) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(orderUpdateRequest(status))
            .when()
            .put("/api/orders/{orderId}/order-status", id)
            .then().log().all()
            .extract();
    }

    public static void 주문_상태_수정_됨(ExtractableResponse<Response> response,
        OrderStatus expectedStatus) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(Order.class))
                .extracting(Order::getOrderStatus)
                .isEqualTo(expectedStatus.name())
        );
    }

    public static Order orderCreateRequest(OrderTable table, OrderLineItem orderLineItem) {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(table.getId());
        return order;
    }

    private static Order orderUpdateRequest(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    private static OrderLineItem orderLineItemRequest(Menu menu, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(menu.getId());
        return orderLineItem;
    }
}
