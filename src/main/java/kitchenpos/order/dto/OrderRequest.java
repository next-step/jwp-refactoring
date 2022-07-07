package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> fetchMenuIds() {
        return Collections.unmodifiableList(
                orderLineItems.stream()
                        .map(OrderLineItemRequest::getMenuId)
                        .collect(Collectors.toList())
        );
    }

    public Order toEntity(final List<Menu> menus) {
        return Order.of(orderTableId, toOrderLineItems(menus));
    }

    private List<OrderLineItem> toOrderLineItems(final List<Menu> menus) {
        return Collections.unmodifiableList(
                orderLineItems.stream()
                        .map(it -> it.toEntity(menus))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderTableId=" + orderTableId +
                ", orderLineItems=" + orderLineItems +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItems);
    }
}
