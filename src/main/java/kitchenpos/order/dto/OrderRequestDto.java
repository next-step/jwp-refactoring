package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequestDto {
    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    public OrderRequestDto() {
    }

    public OrderRequestDto(Long orderTableId, List<OrderLineItemRequestDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequestDto> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Order(orderTableId, orderLineItems);
    }
}
