package kitchenpos.order;

import static kitchenpos.table.TableFixture.일번테이블;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.MenuFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.http.MediaType;

public class OrderFixture {

    public static final OrderLineItem 주문항목 = new OrderLineItem(1L, null, MenuFixture.더블강정치킨.getId(),
        2L);
    public static final Order 주문 = new Order(1L, 일번테이블.getId(), null, null,
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
        OrderRequest order = new OrderRequest(orderStatus.name());

        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/orders/" + orderId + "/order-status")
            .then().log().all()
            .extract();
    }

}
