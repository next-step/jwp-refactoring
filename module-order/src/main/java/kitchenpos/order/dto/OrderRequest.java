package kitchenpos.order.dto;

import static kitchenpos.common.message.ValidationMessage.POSITIVE;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import kitchenpos.common.exception.InvalidMenuNumberException;
import kitchenpos.common.message.ValidationMessage;

public class OrderRequest {
    @Positive(message = POSITIVE)
    private Long orderTableId;

    @Size(min = 1, message = ValidationMessage.MIN_SIZE_IS_ONE)
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
