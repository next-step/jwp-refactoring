package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;

public class OrderAcceptanceAPI {

    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(items -> new OrderLineItemRequest(items.getMenuId(), items.getQuantity()))
                .collect(Collectors.toList());

        OrderRequest order = new OrderRequest(orderTableId, orderLineItemRequests);

        return AcceptanceTest.doPost("/api/orders", order);
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return AcceptanceTest.doGet("/api/orders");
    }

    public static ExtractableResponse<Response> 주문_상태_변경(Long orderId, OrderStatus orderStatus) {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus);

        return AcceptanceTest.doPut("/api/orders/" + orderId + "/order-status", orderStatusRequest);
    }
}
