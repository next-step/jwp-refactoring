package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long menuId;
    private final Long quantity;

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    private OrderLineItemResponse(Long seq, Long menuId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
