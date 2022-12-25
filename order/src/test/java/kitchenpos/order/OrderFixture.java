package kitchenpos.order;

import static kitchenpos.order.TableFixture.일번테이블;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.MenuFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.http.MediaType;

public class OrderFixture {

    public static final OrderLineItem 주문항목 = new OrderLineItem(1L, null,
        OrderMenu.from(MenuFixture.더블강정치킨),
        2L);
    public static final Order 주문 = new Order(1L, 일번테이블, null, null,
        Collections.singletonList(주문항목));
    public static final Order 조리중주문 = new Order(1L, 일번테이블, COOKING, null,
        Collections.singletonList(주문항목));
    public static final Order 식사중 = new Order(1L, 일번테이블, MEAL, null,
        Collections.singletonList(주문항목));

    public static ExtractableResponse<Response> 주문(Long orderTableId,
        List<OrderLineItemRequest> orderLineItems) {
        OrderRequest order = new OrderRequest(orderTableId, orderLineItems);

        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_수정(Long orderId,
        OrderStatus orderStatus) {
        OrderRequest order = new OrderRequest(orderStatus);

        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/orders/" + orderId + "/order-status")
            .then().log().all()
            .extract();
    }

    public static OrderRequest createOrderRequest(Order order) {
        return new OrderRequest(order.orderTableId(), order.getOrderStatus(),
            createOrderLineItemRequest(order.getOrderLineItems()));
    }

    public static List<OrderLineItemRequest> createOrderLineItemRequest(
        List<OrderLineItem> orderLineItem) {
        if (Objects.isNull(orderLineItem)) {
            return null;
        }
        return orderLineItem.stream()
            .map(domain -> new OrderLineItemRequest(domain.menuId(), domain.getQuantity()))
            .collect(Collectors.toList());
    }
}
