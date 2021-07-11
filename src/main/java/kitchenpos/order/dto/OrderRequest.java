package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;
    private OrderStatus orderStatus;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = null;
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItem() {
        return orderLineItems
                .stream()
                .map(v -> {
                    Long quantity = v.getQuantity();
                    return OrderLineItem.builder()
                            .menuId(v.getMenuId())
                            .quantity(quantity)
                            .build();
                }).collect(Collectors.toList());
    }
}
