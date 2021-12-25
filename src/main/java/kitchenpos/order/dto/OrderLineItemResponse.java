package kitchenpos.order.dto;

import java.util.Objects;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(OrderLineItem orderLineItem, Long orderId) {
        this(orderLineItem.getId(), orderId,
            orderLineItem.getMenuId(), orderLineItem.getQuantity().getValue());
    }

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderLineItemResponse that = (OrderLineItemResponse)o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(orderId,
            that.orderId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, menuId, quantity);
    }
}
