package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.table.domain.OrderTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static kitchenpos.menu.domain.QuantityTest.수량_생성;

public class OrderGenerator {
    private static final String PATH = "/api/orders";

    public static Order 주문_생성(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static OrderLineItem 주문_물품_생성(Menu menu, long quantity) {
        return new OrderLineItem(menu, 수량_생성(quantity));
    }

    public static OrderLineItems 주문_물품_목록_생성(OrderLineItem... orderLineItems) {
        return new OrderLineItems(Arrays.asList(orderLineItems));
    }

    public static OrderCreateRequest 주문_생성_요청(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTable, orderLineItems);
    }

    public static OrderLineItemRequest 주문_물품_생성_요청(Long menu, Long quantity) {
         return new OrderLineItemRequest(menu, quantity);
    }

    public static ExtractableResponse<Response> 주문_생성_API_요청(OrderCreateRequest request) {
        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), request);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_API_요청() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 주문_상태_변경_API_요청(Long orderId, OrderStatus orderStatus) {
        Map<String, Object> params = Collections.singletonMap("orderStatus", orderStatus);
        return RestAssuredRequest.putRequest(PATH + "/{orderId}/order-status", params, Collections.emptyMap(), orderId);
    }
}
