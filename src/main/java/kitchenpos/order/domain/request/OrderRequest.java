package kitchenpos.order.domain.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<Long> orderLineItemIds;

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<Long> orderLineItemIds) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemIds = orderLineItemIds;
    }
}
