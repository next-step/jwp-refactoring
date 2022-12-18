package kitchenpos.dto;

import kitchenpos.domain.*;
import kitchenpos.domain.type.OrderStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequest;

    protected OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequest) {
        if (Objects.isNull(orderLineItemRequest)) {
            throw new IllegalArgumentException();
        }

        this.orderTableId = orderTableId;
        this.orderLineItemRequest = orderLineItemRequest;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<Long> getMenuId() {
        return orderLineItemRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Order makeOrder(OrderTable orderTable, List<Menu> menus) {
        Order order = new Order(orderTable, OrderStatus.COOKING);

        List<OrderLineItem> orderLineItems = getOrderLineItemRequest().stream()
                .map(item -> item.createOrderLineItemRequest(menus))
                .collect(Collectors.toList());

        order.addOrderLineItems(orderLineItems, menus);

        return order;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequest() {
        return orderLineItemRequest;
    }

}
