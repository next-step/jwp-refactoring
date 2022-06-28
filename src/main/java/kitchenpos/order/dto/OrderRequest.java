package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import kitchenpos.exception.InvalidMenuNumberException;

public class OrderRequest {
    private Long orderTableId;
    @Size(min = 1)
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getOrderMenuIds() {
        if (this.orderLineItems.isEmpty()) {
            throw new InvalidMenuNumberException();
        }

        return this.orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
