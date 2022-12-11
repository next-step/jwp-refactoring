package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {

    public static OrderRequest createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return OrderRequest.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderRequest createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return OrderRequest.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
