package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderAcceptanceTestHelper {
    public static ExtractableResponse<Response> 주문_상태_변경_요청(Order order, OrderStatus orderStatus) {
        order.setOrderStatus(orderStatus.name());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().put("/api/orders/{orderId}/order-status", order.getId())
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all().extract();
    }

    public static OrderLineItem 주문_항목(Menu menu, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static ExtractableResponse<Response> 주문_생성_요청(long orderTableId, OrderStatus orderStatus,
        OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderLineItems(Arrays.asList(orderLineItems));

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().post("/api/orders")
            .then().log().all().extract();
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isNotEqualTo(orderStatus.name());
    }

    public static void 주문_생성됨(ExtractableResponse<Response> 주문_생성_요청_응답) {
        assertThat(주문_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Order> orders = response.as(List.class);
        assertThat(orders.size()).isNotZero();
    }
}
