package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequestTest {

    public static OrderRequest 주문_생성_요청_객체_생성(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest.Builder()
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderLineItemRequests(orderLineItemRequests)
                .build();
    }
}