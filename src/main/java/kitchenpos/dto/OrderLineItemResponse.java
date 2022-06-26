package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long id;
    private Long menuId;
    private Long orderId;
    private int quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long id, Long menuId, Long orderId, int quantity) {
        this.id = id;
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getOrderId(),
                (int) orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return quantity == that.quantity
                && Objects.equals(id, that.id)
                && Objects.equals(menuId, that.menuId)
                && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, orderId, quantity);
    }
}
