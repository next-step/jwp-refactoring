package kitchenpos.fixture;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {

    public static OrderRequest 주문(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        return OrderRequest.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
