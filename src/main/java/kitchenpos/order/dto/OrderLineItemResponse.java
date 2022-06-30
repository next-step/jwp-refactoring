package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private Long orderId;
    private int quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long menuId, Long orderId, int quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenu().getId(),
                orderLineItem.getOrder().getId(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
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
                && Objects.equals(seq, that.seq)
                && Objects.equals(menuId, that.menuId)
                && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, orderId, quantity);
    }
}
